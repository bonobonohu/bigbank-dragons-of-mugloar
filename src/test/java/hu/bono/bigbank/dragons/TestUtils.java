package hu.bono.bigbank.dragons;

import hu.bono.bigbank.dragons.common.application.ApiConfiguration;
import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.game.application.PostGameStartResponse;
import hu.bono.bigbank.dragons.investigate.application.PostInvestigateReputationResponse;
import hu.bono.bigbank.dragons.investigate.domain.Reputation;

import java.time.Instant;

public class TestUtils {

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

    public static PostGameStartResponse createPostGameStartResponse(String gameId) {
        return PostGameStartResponse.builder()
            .gameId(gameId)
            .lives(3)
            .gold(0)
            .level(0)
            .score(0)
            .highScore(0)
            .turn(0)
            .build();
    }

    public static PostGameStartResponse createPostGameStartResponse() {
        return createPostGameStartResponse("GameId123");
    }

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

    public static PostInvestigateReputationResponse createPostInvestigateReputationResponse(
        Double people,
        Integer state
    ) {
        return PostInvestigateReputationResponse.builder()
            .people(people)
            .state(state)
            .underworld(0)
            .build();
    }

    public static PostInvestigateReputationResponse createPostInvestigateReputationResponse() {
        return createPostInvestigateReputationResponse(2.2, -1);
    }

    public static Reputation createReputation(Double people, Integer state) {
        return Reputation.builder()
            .people(people)
            .state(state)
            .underworld(0)
            .build();
    }

    public static Reputation createReputation() {
        return createReputation(2.2, -1);
    }
}
