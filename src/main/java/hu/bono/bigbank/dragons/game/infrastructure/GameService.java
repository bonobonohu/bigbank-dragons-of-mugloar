package hu.bono.bigbank.dragons.game.infrastructure;

import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.game.application.GameClient;
import hu.bono.bigbank.dragons.game.application.PostGameStartResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameClient gameClient;

    public GameSession gameStart() {
        final PostGameStartResponse postGameStartResponse = gameClient.postGameStart();
        final GameSession gameSession =
            PostGameStartResponseMapper.MAPPER.postGameStartResponseToGameSession(postGameStartResponse);
        gameSession.getLogFileName();
        return gameSession;
    }
}
