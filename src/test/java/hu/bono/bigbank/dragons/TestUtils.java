package hu.bono.bigbank.dragons;

import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.game.application.PostGameResponse;

import java.time.Instant;

public class TestUtils {

    public static GameSession createGameSession(Instant timestamp, String gameId) {
        return new GameSession(
            timestamp,
            gameId,
            3,
            0,
            0,
            0,
            0,
            0
        );
    }

    public static GameSession createGameSession(Instant timestamp) {
        return createGameSession(timestamp, "GameId123");
    }

    public static PostGameResponse createPostGameResponse(String gameId) {
        return new PostGameResponse(
            gameId,
            3,
            0,
            0,
            0,
            0,
            0
        );
    }

    public static PostGameResponse createPostGameResponse() {
        return createPostGameResponse("GameId123");
    }
}
