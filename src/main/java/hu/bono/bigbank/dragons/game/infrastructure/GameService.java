package hu.bono.bigbank.dragons.game.infrastructure;

import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.game.application.GameClient;
import hu.bono.bigbank.dragons.game.application.PostGameResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameClient gameClient;

    public GameSession startGame() {
        final PostGameResponse postGameResponse = gameClient.postGame();
        final GameSession gameSession = PostGameResponseMapper.MAPPER.postGameResponseToGameSession(postGameResponse);
        gameSession.getLogFileName();
        return gameSession;
    }
}
