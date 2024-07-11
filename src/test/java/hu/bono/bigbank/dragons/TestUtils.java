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

    public static GetMessagesResponseItem createGetMessagesResponseItem(
        String adId,
        String message,
        String probability
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
        Integer encrypted
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
        String adId,
        String message,
        String probability,
        Integer encrypted
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
        String adId,
        String message,
        Message.Probability probability
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
        String adId,
        String message,
        Message.Probability probability,
        Boolean wasEncrypted
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
        Boolean success,
        Integer lives,
        Integer gold,
        Integer score,
        Integer turn
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
        Boolean success,
        Integer lives,
        Integer gold,
        Integer score,
        Integer turn
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
        String id,
        String name,
        Integer cost
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
        String id,
        String name,
        Integer cost
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
        Boolean shoppingSuccess,
        Integer gold,
        Integer lives,
        Integer level,
        Integer turn
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
        Boolean shoppingSuccess,
        Integer gold,
        Integer lives,
        Integer level,
        Integer turn
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
