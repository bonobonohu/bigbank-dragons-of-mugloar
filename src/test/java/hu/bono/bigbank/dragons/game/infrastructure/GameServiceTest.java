package hu.bono.bigbank.dragons.game.infrastructure;

import hu.bono.bigbank.dragons.TestUtils;
import hu.bono.bigbank.dragons.game.application.GameClient;
import hu.bono.bigbank.dragons.game.application.PostGameStartResponse;
import hu.bono.bigbank.dragons.game.domain.Game;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GameServiceTest {

    private final GameClient gameClient = Mockito.mock(GameClient.class);
    private final GameService underTest = new GameService(gameClient);

    @BeforeEach
    void beforeEach() {
        Mockito.reset(gameClient);
    }

    @Test
    void testGameStart() {
        final PostGameStartResponse postGameStartResponse = TestUtils.createPostGameStartResponse("GameId123");
        final Game expected = TestUtils.createGame("GameId123");
        Mockito.when(gameClient.postGameStart())
            .thenReturn(postGameStartResponse);
        final Game actual = underTest.gameStart();
        Assertions.assertThat(actual).isEqualTo(expected);
        Mockito.verify(gameClient)
            .postGameStart();
    }
}
