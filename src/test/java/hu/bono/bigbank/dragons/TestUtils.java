package hu.bono.bigbank.dragons;

import hu.bono.bigbank.dragons.common.application.ApiConfiguration;
import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.game.application.PostGameStartResponse;
import hu.bono.bigbank.dragons.investigation.application.PostInvestigateReputationResponse;
import hu.bono.bigbank.dragons.investigation.domain.Reputation;
import hu.bono.bigbank.dragons.message.application.GetMessagesResponseItem;
import hu.bono.bigbank.dragons.message.application.PostSolveAdResponse;
import hu.bono.bigbank.dragons.message.domain.Message;
import hu.bono.bigbank.dragons.message.domain.MissionOutcome;
import hu.bono.bigbank.dragons.shop.application.GetShopResponseItem;
import hu.bono.bigbank.dragons.shop.application.PostShopBuyItemResponse;
import hu.bono.bigbank.dragons.shop.domain.PurchaseOutcome;
import hu.bono.bigbank.dragons.shop.domain.ShopItem;

import java.time.Instant;
import java.util.List;

public final class TestUtils {

    private TestUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
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

    public static PostGameStartResponse createPostGameStartResponse(
        final String gameId
    ) {
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

    public static GameSession createGameSession(
        final Instant timestamp,
        final String gameId
    ) {
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

    public static GameSession createGameSession(
        final Instant timestamp
    ) {
        return createGameSession(timestamp, "GameId123");
    }

    public static PostInvestigateReputationResponse createPostInvestigateReputationResponse(
        final Double people,
        final Integer state
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

    public static Reputation createReputation(
        final Double people,
        final Integer state
    ) {
        return Reputation.builder()
            .people(people)
            .state(state)
            .underworld(0)
            .build();
    }

    public static Reputation createReputation() {
        return createReputation(2.2, -1);
    }

    public static GetMessagesResponseItem createGetMessagesResponseItem(
        final String adId,
        final String message,
        final String probability
    ) {
        return GetMessagesResponseItem.builder()
            .adId(adId)
            .message(message)
            .reward(100)
            .expiresIn(10)
            .encrypted(null)
            .probability(probability)
            .build();
    }

    public static GetMessagesResponseItem createGetMessagesResponseItem(
        final Integer encrypted
    ) {
        return GetMessagesResponseItem.builder()
            .adId("AdId123")
            .message("Help Tarquinius Reynell to fix their bucket")
            .reward(100)
            .expiresIn(10)
            .encrypted(encrypted)
            .probability("Piece of cake")
            .build();
    }

    public static GetMessagesResponseItem createGetMessagesResponseItem(
        final String adId,
        final String message,
        final String probability,
        final Integer encrypted
    ) {
        return GetMessagesResponseItem.builder()
            .adId(adId)
            .message(message)
            .reward(100)
            .expiresIn(10)
            .encrypted(encrypted)
            .probability(probability)
            .build();
    }

    public static GetMessagesResponseItem createGetMessagesResponseItem() {
        return createGetMessagesResponseItem(
            "AdId123",
            "Help Tarquinius Reynell to fix their bucket",
            "Piece of cake"
        );
    }

    public static GetMessagesResponseItem createEncryptedGetMessagesResponseItem() {
        return createGetMessagesResponseItem(
            "dWFMamdrcmk=",
            "SW5maWx0cmF0ZSBUaGUgRWFnbGUgU29sZGllcnMgYW5kIHJlY292ZXIgdGhlaXIgc2VjcmV0cy4=",
            "UGxheWluZyB3aXRoIGZpcmU=",
            1
        );
    }

    public static List<GetMessagesResponseItem> createGetMessagesResponseItems() {
        return List.of(
            createGetMessagesResponseItem(),
            createEncryptedGetMessagesResponseItem()
        );
    }

    public static Message createMessage(
        final String adId,
        final String message,
        final Message.Probability probability
    ) {
        return Message.builder()
            .adId(adId)
            .message(message)
            .reward(100)
            .expiresIn(10)
            .wasEncrypted(false)
            .probability(probability)
            .build();
    }

    public static Message createMessage(
        final String adId,
        final String message,
        final Message.Probability probability,
        final Boolean wasEncrypted
    ) {
        return Message.builder()
            .adId(adId)
            .message(message)
            .reward(100)
            .expiresIn(10)
            .wasEncrypted(wasEncrypted)
            .probability(probability)
            .build();
    }

    public static Message createMessage() {
        return createMessage(
            "AdId123",
            "Help Tarquinius Reynell to fix their bucket",
            Message.Probability.PIECE_OF_CAKE
        );
    }

    public static Message createWasEncryptedMessage() {
        return createMessage(
            "uaLjgkri",
            "Infiltrate The Eagle Soldiers and recover their secrets.",
            Message.Probability.PLAYING_WITH_FIRE,
            true
        );
    }

    public static List<Message> createMessages() {
        return List.of(
            createMessage(),
            createWasEncryptedMessage()
        );
    }

    public static PostSolveAdResponse createPostSolveAdResponse(
        final Boolean success,
        final Integer lives,
        final Integer gold,
        final Integer score,
        final Integer turn
    ) {
        return PostSolveAdResponse.builder()
            .success(success)
            .lives(lives)
            .gold(gold)
            .score(score)
            .highScore(0)
            .turn(turn)
            .message("You successfully solved the mission!")
            .build();
    }

    public static PostSolveAdResponse createPostSolveAdResponse() {
        return createPostSolveAdResponse(true, 3, 100, 200, 42);
    }

    public static MissionOutcome createMissionOutcome(
        final Boolean success,
        final Integer lives,
        final Integer gold,
        final Integer score,
        final Integer turn
    ) {
        return MissionOutcome.builder()
            .success(success)
            .lives(lives)
            .gold(gold)
            .score(score)
            .highScore(0)
            .turn(turn)
            .message("You successfully solved the mission!")
            .build();
    }

    public static MissionOutcome createMissionOutcome() {
        return createMissionOutcome(true, 3, 100, 200, 42);
    }

    public static GetShopResponseItem createGetShopResponseItem(
        final String id,
        final String name,
        final Integer cost
    ) {
        return GetShopResponseItem.builder()
            .id(id)
            .name(name)
            .cost(cost)
            .build();
    }


    public static GetShopResponseItem createGetShopResponseItem() {
        return createGetShopResponseItem(
            "hpot",
            "Healing potion",
            50
        );
    }

    public static List<GetShopResponseItem> createGetShopResponseItems() {
        return List.of(
            createGetShopResponseItem(),
            createGetShopResponseItem("gas", "Gasoline", 100)
        );
    }

    public static ShopItem createShopItem(
        final String id,
        final String name,
        final Integer cost
    ) {
        return ShopItem.builder()
            .id(id)
            .name(name)
            .cost(cost)
            .build();
    }

    public static ShopItem createShopItem() {
        return createShopItem(
            "hpot",
            "Healing potion",
            50
        );
    }

    public static List<ShopItem> createShopItems() {
        return List.of(
            createShopItem(),
            createShopItem("gas", "Gasoline", 100)
        );
    }

    public static PostShopBuyItemResponse createPostShopBuyItemResponse(
        final Boolean shoppingSuccess,
        final Integer gold,
        final Integer lives,
        final Integer level,
        final Integer turn
    ) {
        return PostShopBuyItemResponse.builder()
            .shoppingSuccess(shoppingSuccess)
            .gold(gold)
            .lives(lives)
            .level(level)
            .turn(turn)
            .build();
    }

    public static PostShopBuyItemResponse createPostShopBuyItemResponse() {
        return createPostShopBuyItemResponse(true, 100, 3, 2, 42);
    }

    public static PurchaseOutcome createPurchaseOutcome(
        final Boolean shoppingSuccess,
        final Integer gold,
        final Integer lives,
        final Integer level,
        final Integer turn
    ) {
        return PurchaseOutcome.builder()
            .shoppingSuccess(shoppingSuccess)
            .gold(gold)
            .lives(lives)
            .level(level)
            .turn(turn)
            .build();
    }

    public static PurchaseOutcome createPurchaseOutcome() {
        return createPurchaseOutcome(true, 100, 3, 2, 42);
    }
}
