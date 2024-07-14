package hu.bono.bigbank.dragons.common.infrastructure;

import hu.bono.bigbank.dragons.TestUtils;
import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.common.domain.LogWriterException;
import hu.bono.bigbank.dragons.shop.domain.PurchaseOutcome;
import hu.bono.bigbank.dragons.shop.domain.ShopItem;
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
    private static final GameSession GAME_SESSION = TestUtils.createGameSession(TIMESTAMP, GAME_ID);
    private static final ShopItem SHOP_ITEM = TestUtils.createShopItem();
    private static final PurchaseOutcome PURCHASE_OUTCOME = TestUtils.createPurchaseOutcome();
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
    void beforeEach() throws IOException {
        Files.deleteIfExists(LOG_FILE_PATH);
        Files.deleteIfExists(LOG_DIR_PATH);
        Mockito.reset(resourceFactory);
    }

    @AfterEach
    void afterEach() throws IOException {
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
                () -> underTest.log(GAME_SESSION, "startGame", null, GAME_SESSION)
            )
            .isInstanceOf(LogWriterException.class);
    }

    @Test
    void testLogCreatesFileWithHeaderAndEntriesWhenInputObjectIsNull() throws IOException {
        underTest.log(GAME_SESSION, "startGame", null, GAME_SESSION);
        underTest.log(GAME_SESSION, "endGame", null, GAME_SESSION);
        final List<String> lines = Files.readAllLines(LOG_FILE_PATH);
        Assertions.assertThat(lines).hasSize(3);
        Assertions.assertThat(lines.get(0)).contains("Timestamp", "GameId", "Event", "Input", "Output");
        Assertions.assertThat(
            lines.get(1)).contains(GAME_ID, "startGame", "null", GAME_SESSION.toString()
        );
        Assertions.assertThat(
            lines.get(2)).contains(GAME_ID, "endGame", "null", GAME_SESSION.toString()
        );
    }

    @Test
    void testLogCreatesFileWithHeaderAndEntriesWhenInputObjectIsNotNull() throws IOException {
        underTest.log(GAME_SESSION, "purchaseItem1", SHOP_ITEM, PURCHASE_OUTCOME);
        underTest.log(GAME_SESSION, "purchaseItem2", SHOP_ITEM, PURCHASE_OUTCOME);
        final List<String> lines = Files.readAllLines(LOG_FILE_PATH);
        Assertions.assertThat(lines).hasSize(3);
        Assertions.assertThat(lines.get(0)).contains("Timestamp", "GameId", "Event", "Input", "Output");
        Assertions.assertThat(
            lines.get(1)).contains(GAME_ID, "purchaseItem1", SHOP_ITEM.toString(), PURCHASE_OUTCOME.toString()
        );
        Assertions.assertThat(
            lines.get(2)).contains(GAME_ID, "purchaseItem2", SHOP_ITEM.toString(), PURCHASE_OUTCOME.toString()
        );
    }
}
