package hu.bono.bigbank.dragons.game.infrastructure;

import hu.bono.bigbank.dragons.game.application.GameClient;
import hu.bono.bigbank.dragons.game.application.PostGameStartResponse;
import hu.bono.bigbank.dragons.game.application.PostGameStartResponseMapper;
import hu.bono.bigbank.dragons.game.domain.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameClient gameClient;

    public Game startGame() {
        final PostGameStartResponse postGameStartResponse = gameClient.postGameStart();
        return PostGameStartResponseMapper.MAPPER
            .postGameStartResponseToGame(postGameStartResponse);
    }
}
