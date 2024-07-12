package hu.bono.bigbank.dragons.game.infrastructure;

import hu.bono.bigbank.dragons.TestUtils;
import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.common.infrastructure.LogWriter;
import hu.bono.bigbank.dragons.game.application.GameClient;
import hu.bono.bigbank.dragons.game.application.PostGameStartResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;

class GameServiceTest {

    private final GameClient gameClient = Mockito.mock(GameClient.class);
    private final LogWriter logWriter = Mockito.mock(LogWriter.class);
    private final GameService underTest = new GameService(gameClient, logWriter);

    @BeforeEach
    void beforeEach() {
        Mockito.reset(gameClient, logWriter);
    }

    @Test
    void testGameStart() {
        final PostGameStartResponse postGameStartResponse = TestUtils.createPostGameStartResponse("GameId123");
        final GameSession expected = TestUtils.createGameSession(Instant.now(), "GameId123");
        Mockito.when(gameClient.postGameStart())
            .thenReturn(postGameStartResponse);
        final GameSession actual = underTest.gameStart();
        Assertions.assertThat(actual.getGameId()).isEqualTo(expected.getGameId());
        Mockito.verify(gameClient, Mockito.times(1))
            .postGameStart();
        Mockito.verify(logWriter, Mockito.times(1))
            .log(actual, "gameStart", "New game started", postGameStartResponse);
    }
}
