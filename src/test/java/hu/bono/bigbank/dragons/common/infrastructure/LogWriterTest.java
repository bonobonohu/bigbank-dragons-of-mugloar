package hu.bono.bigbank.dragons.common.infrastructure;

import hu.bono.bigbank.dragons.TestUtils;
import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.common.domain.LogWriterException;
import hu.bono.bigbank.dragons.game.application.PostGameStartResponse;
import org.apache.commons.csv.CSVPrinter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;

import static hu.bono.bigbank.dragons.common.infrastructure.LogWriter.*;
import static org.mockito.ArgumentMatchers.any;

class LogWriterTest {

    private static final Instant TIMESTAMP = Instant.parse("2024-07-09T20:27:42Z");
    private static final String GAME_ID = "GameId123";
    private static final PostGameStartResponse POST_GAME_START_RESPONSE = TestUtils.createPostGameStartResponse(GAME_ID);
    private static final GameSession GAME_SESSION = TestUtils.createGameSession(TIMESTAMP, GAME_ID);
    private static final Path LOG_DIR_PATH = Paths.get(
        USER_DIR,
        LOG_DIR,
        DATE_FORMATTER.format(GAME_SESSION.getCreationTimestamp())
    );
    private static final Path LOG_FILE_PATH = Paths.get(
        USER_DIR,
        LOG_DIR,
        DATE_FORMATTER.format(GAME_SESSION.getCreationTimestamp()),
        GAME_SESSION.getLogFileName() + LOG_FILE_EXTENSION
    );

    private final ResourceFactory resourceFactory = Mockito.spy(ResourceFactory.class);

    private final LogWriter underTest = new LogWriter(resourceFactory);

    @BeforeEach
    public void beforeEach() throws IOException {
        Files.deleteIfExists(LOG_FILE_PATH);
        Files.deleteIfExists(LOG_DIR_PATH);
        Mockito.reset(resourceFactory);
    }

    @AfterEach
    public void afterEach() throws IOException {
        Files.deleteIfExists(LOG_FILE_PATH);
        Files.deleteIfExists(LOG_DIR_PATH);
    }

    @Test
    void testLogThrowsLogWriterExceptionWhenIOExceptionThrown() throws IOException {
        final CSVPrinter csvPrinter = Mockito.mock(CSVPrinter.class);
        Mockito.doThrow(IOException.class)
            .when(csvPrinter).flush();
        Mockito.doReturn(csvPrinter)
            .when(resourceFactory).createCSVPrinter(any());

        Assertions.assertThatThrownBy(
            () -> underTest.log(GAME_SESSION, "gameStart", "New game started", POST_GAME_START_RESPONSE)
        ).isInstanceOf(LogWriterException.class);
    }

    @Test
    void testLogCreatesFileWithHeaderAndEntries() throws IOException {
        underTest.log(GAME_SESSION, "gameStart", "New game started", POST_GAME_START_RESPONSE);
        underTest.log(GAME_SESSION, "gameEnd", "Game ended", POST_GAME_START_RESPONSE);

        final List<String> lines = Files.readAllLines(LOG_FILE_PATH);

        Assertions.assertThat(lines).hasSize(3);
        Assertions.assertThat(lines.get(0)).contains("Timestamp", "GameId", "Event", "Details", "Response");
        Assertions.assertThat(lines.get(1)).contains(GAME_ID, "gameStart", "New game started", POST_GAME_START_RESPONSE.toString());
        Assertions.assertThat(lines.get(2)).contains(GAME_ID, "gameEnd", "Game ended", POST_GAME_START_RESPONSE.toString());
    }
}
