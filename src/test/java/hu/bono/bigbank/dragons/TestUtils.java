package hu.bono.bigbank.dragons;

import hu.bono.bigbank.dragons.common.application.ApiConfiguration;
import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.game.application.PostGameResponse;

import java.time.Instant;

public class TestUtils {

    public static GameSession createGameSession(Instant timestamp, String gameId) {
        return GameSession.builder()
            .creationTimestamp(timestamp)
            .gameId(gameId)
            .lives(3)
            .gold(0)
            .level(0)
            .score(0)
            .highScore(0)
            .turn(0)
            .build();
    }

    public static GameSession createGameSession(Instant timestamp) {
        return createGameSession(timestamp, "GameId123");
    }

    public static PostGameResponse createPostGameResponse(String gameId) {
        return PostGameResponse.builder()
            .gameId(gameId)
            .lives(3)
            .gold(0)
            .level(0)
            .score(0)
            .highScore(0)
            .turn(0)
            .build();
    }

    public static PostGameResponse createPostGameResponse() {
        return createPostGameResponse("GameId123");
    }

    public static ApiConfiguration createApiConfiguration() {
        return ApiConfiguration.builder()
            .baseUrl("https://dragonsofmugloar.com/api/v2")
            .endpoints(
                ApiConfiguration.Endpoints.builder()
                    .gameStart("/game/start")
                    .investigateReputation("/{gameId}/investigate/reputation")
                    .messages("/{gameId}/messages")
                    .solveAd("/{gameId}/solve/{adId}")
                    .shop("/{gameId}/shop")
                    .shopBuyItem("/{gameId}/shop/buy/{itemId}")
                    .build()
            )
            .build();
    }
}
