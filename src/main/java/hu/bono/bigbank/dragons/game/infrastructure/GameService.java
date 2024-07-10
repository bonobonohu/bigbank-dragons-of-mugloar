package hu.bono.bigbank.dragons.game.infrastructure;

import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.common.infrastructure.LogWriter;
import hu.bono.bigbank.dragons.game.application.GameClient;
import hu.bono.bigbank.dragons.game.application.PostGameStartResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameClient gameClient;
    private final LogWriter logWriter;

    public GameSession gameStart() {
        final PostGameStartResponse postGameStartResponse = gameClient.postGameStart();
        final GameSession gameSession = PostGameStartResponseMapper.MAPPER
            .postGameStartResponseToGameSession(postGameStartResponse);
        logWriter.log(gameSession, "gameStart", "New game started", postGameStartResponse);
        return gameSession;
    }
}
