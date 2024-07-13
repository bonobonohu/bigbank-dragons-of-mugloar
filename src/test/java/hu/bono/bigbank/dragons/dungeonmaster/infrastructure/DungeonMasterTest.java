package hu.bono.bigbank.dragons.dungeonmaster.infrastructure;

import hu.bono.bigbank.dragons.TestUtils;
import hu.bono.bigbank.dragons.common.domain.CharacterSheet;
import hu.bono.bigbank.dragons.common.domain.GameMapper;
import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.common.infrastructure.Api;
import hu.bono.bigbank.dragons.common.infrastructure.LogWriter;
import hu.bono.bigbank.dragons.game.domain.Game;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;

class DungeonMasterTest {

    private final Api api = Mockito.mock(Api.class);
    private final GameMapper gameMapper = Mockito.mock(GameMapper.class);
    private final LogWriter logWriter = Mockito.mock(LogWriter.class);
    private final DungeonMaster underTest = new DungeonMaster(api, gameMapper, logWriter);

    @BeforeEach
    void beforeEach() {
        Mockito.reset(api, gameMapper, logWriter);
    }

    @Test
    void testStartGame() {
        final CharacterSheet characterSheet = TestUtils.createCharacterSheet("Joseph Cornelius Hallenbeck");
        final Game game = TestUtils.createGame("GameId123");
        final GameSession expected = TestUtils.createGameSession(Instant.now(), "GameId123");
        Mockito.when(api.gameStart())
            .thenReturn(game);
        Mockito.when(gameMapper.gameToGameSession(game, characterSheet))
            .thenReturn(expected);
        final GameSession actual = underTest.startGame(characterSheet);
        Assertions.assertThat(actual.getGameId()).isEqualTo(expected.getGameId());
        Mockito.verify(api)
            .gameStart();
        Mockito.verify(logWriter)
            .log(actual, "startGame", "New game started", actual);
    }
}
