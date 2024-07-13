package hu.bono.bigbank.dragons.dungeonmaster.infrastructure;

import hu.bono.bigbank.dragons.common.domain.CharacterSheet;
import hu.bono.bigbank.dragons.common.domain.GameMapper;
import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.common.infrastructure.Api;
import hu.bono.bigbank.dragons.common.infrastructure.LogWriter;
import hu.bono.bigbank.dragons.game.domain.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DungeonMaster {

    private final Api api;
    private final GameMapper gameMapper;
    private final LogWriter logWriter;

    public GameSession startGame(
        final CharacterSheet characterSheet
    ) {
        final Game game = api.gameStart();
        final GameSession gameSession = gameMapper.gameToGameSession(game, characterSheet);
        logWriter.log(gameSession, "startGame", "New game started", gameSession);
        return gameSession;
    }
}
