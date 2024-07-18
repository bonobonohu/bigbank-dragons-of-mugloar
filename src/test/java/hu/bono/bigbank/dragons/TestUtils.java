package hu.bono.bigbank.dragons;

import hu.bono.bigbank.dragons.common.application.ApiConfiguration;
import hu.bono.bigbank.dragons.common.domain.CharacterSheet;
import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.game.application.PostGameStartResponse;
import hu.bono.bigbank.dragons.game.domain.Game;
import hu.bono.bigbank.dragons.investigation.application.PostInvestigateReputationResponse;
import hu.bono.bigbank.dragons.investigation.domain.Reputation;
import hu.bono.bigbank.dragons.mission.application.GetMessagesResponseItem;
import hu.bono.bigbank.dragons.mission.application.PostSolveAdResponse;
import hu.bono.bigbank.dragons.mission.domain.Message;
import hu.bono.bigbank.dragons.mission.domain.MissionOutcome;
import hu.bono.bigbank.dragons.shop.application.GetShopResponseItem;
import hu.bono.bigbank.dragons.shop.application.PostShopBuyItemResponse;
import hu.bono.bigbank.dragons.shop.domain.PurchaseOutcome;
import hu.bono.bigbank.dragons.shop.domain.Shop;
import hu.bono.bigbank.dragons.shop.domain.ShopItem;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public final class TestUtils {

    public static final Map<String, ShopItem> SHOP_ITEMS = new HashMap<>();
    public static final int HEALING_POT_COST = 50;
    public static final ShopItem HEALING_POT =
        createShopItem(ShopItem.HEALING_POT_ID, "Healing potion", HEALING_POT_COST);
    public static final ShopItem CLAW_SHARPENING =
        createShopItem("cs", "Claw Sharpening", ShopItem.LEVEL_ONE_ITEM_COST);

    static {
        SHOP_ITEMS.put(ShopItem.HEALING_POT_ID, HEALING_POT);
        SHOP_ITEMS.put("cs", CLAW_SHARPENING);
        SHOP_ITEMS.put("gas", createShopItem("gas", "Gas", ShopItem.LEVEL_ONE_ITEM_COST));
        SHOP_ITEMS.put("rf", createShopItem("rf", "Rocket Fuel", ShopItem.LEVEL_TWO_ITEM_COST));
        SHOP_ITEMS.put("iron", createShopItem("iron", "Iron Plating", ShopItem.LEVEL_TWO_ITEM_COST));
    }

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

    public static CharacterSheet.MyBook createCharacterSheetMyBook(
        final Integer lives
    ) {
        return CharacterSheet.MyBook.builder()
            .lives(3)
            .gold(0)
            .level(0)
            .score(0)
            .build();
    }

    public static CharacterSheet.MyBook clone(
        final CharacterSheet.MyBook characterSheetMyBook
    ) {
        return CharacterSheet.MyBook.builder()
            .lives(characterSheetMyBook.getLives())
            .gold(characterSheetMyBook.getGold())
            .level(characterSheetMyBook.getLevel())
            .score(characterSheetMyBook.getScore())
            .build();
    }

    public static CharacterSheet createCharacterSheet(
        final String name,
        final Reputation reputation
    ) {
        return CharacterSheet.builder()
            .name(name)
            .lives(3)
            .gold(0)
            .level(0)
            .score(0)
            .highScore(0)
            .reputation(reputation)
            .myBook(createCharacterSheetMyBook(3))
            .build();
    }

    public static CharacterSheet createCharacterSheet(
        final String name
    ) {
        return createCharacterSheet(name, Reputation.builder().build());
    }

    public static CharacterSheet createCharacterSheet() {
        return createCharacterSheet("Joseph Cornelius Hallenbeck");
    }

    public static CharacterSheet clone(
        final CharacterSheet characterSheet
    ) {
        return CharacterSheet.builder()
            .name(characterSheet.getName())
            .lives(characterSheet.getLives())
            .gold(characterSheet.getGold())
            .level(characterSheet.getLevel())
            .score(characterSheet.getScore())
            .highScore(characterSheet.getHighScore())
            .reputation(clone(characterSheet.getReputation()))
            .myBook(clone(characterSheet.getMyBook()))
            .build();
    }

    public static Shop createShop() {
        return new Shop();
    }

    public static Shop clone(
        final Shop shop
    ) {
        return new Shop().setItems(shop.getItems());
    }

    public static GameSession.MyBook createGameSessionMyBook() {
        return GameSession.MyBook.builder()
            .turn(0)
            .build();
    }

    public static GameSession.MyBook clone(
        final GameSession.MyBook gameSessionMyBook
    ) {
        return GameSession.MyBook.builder()
            .turn(gameSessionMyBook.getTurn())
            .build();
    }

    public static GameSession createGameSession(
        final Instant creationTimestamp,
        final String gameId,
        final CharacterSheet characterSheet,
        final Shop shop
    ) {
        return GameSession.builder()
            .creationTimestamp(creationTimestamp)
            .gameId(gameId)
            .characterSheet(characterSheet)
            .shop(shop)
            .turn(0)
            .purchasedItems(new HashSet<>())
            .messages(new HashSet<>())
            .myBook(createGameSessionMyBook())
            .build();
    }

    public static GameSession createGameSession(
        final CharacterSheet characterSheet
    ) {
        return createGameSession(
            Instant.now(),
            "GameId123",
            characterSheet,
            createShop()
        );
    }

    public static GameSession createGameSession(
        final Instant creationTimestamp,
        final String gameId
    ) {
        return createGameSession(
            creationTimestamp,
            gameId,
            createCharacterSheet(),
            createShop()
        );
    }

    public static GameSession createGameSession(
        final Instant creationTimestamp
    ) {
        return createGameSession(creationTimestamp, "GameId123");
    }

    public static GameSession clone(
        final GameSession gameSession
    ) {
        return GameSession.builder()
            .creationTimestamp(Instant.from(gameSession.getCreationTimestamp()))
            .gameId(gameSession.getGameId())
            .characterSheet(clone(gameSession.getCharacterSheet()))
            .shop(clone(gameSession.getShop()))
            .turn(gameSession.getTurn())
            .purchasedItems(new HashSet<>(gameSession.getPurchasedItems()))
            .messages(new HashSet<>(gameSession.getMessages()))
            .myBook(clone(gameSession.getMyBook()))
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

    public static Game createGame(
        final String gameId
    ) {
        return Game.builder()
            .gameId(gameId)
            .lives(3)
            .gold(0)
            .level(0)
            .score(0)
            .highScore(0)
            .turn(0)
            .build();
    }

    public static Game createGame() {
        return createGame("GameId123");
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

    public static Reputation clone(
        final Reputation reputation
    ) {
        return Reputation.builder()
            .people(reputation.people())
            .state(reputation.state())
            .underworld(reputation.underworld())
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
            .expiresIn(7)
            .encrypted(encrypted)
            .probability(probability)
            .build();
    }

    public static GetMessagesResponseItem createGetMessagesResponseItem(
        final String adId,
        final String message,
        final String probability
    ) {
        return createGetMessagesResponseItem(adId, message, probability, null);
    }

    public static GetMessagesResponseItem createGetMessagesResponseItem() {
        return createGetMessagesResponseItem(
            "AdId123",
            "Help Tarquinius Reynell to fix their bucket",
            "Piece of cake"
        );
    }

    public static GetMessagesResponseItem createBase64EncryptedGetMessagesResponseItem() {
        return createGetMessagesResponseItem(
            "dWFMamdrcmk=",
            "SW5maWx0cmF0ZSBUaGUgRWFnbGUgU29sZGllcnMgYW5kIHJlY292ZXIgdGhlaXIgc2VjcmV0cy4=",
            "UGxheWluZyB3aXRoIGZpcmU=",
            1
        );
    }

    public static GetMessagesResponseItem createRot13EncryptedGetMessagesResponseItem() {
        return createGetMessagesResponseItem(
            "0C7jdIwQ",
            "Xvyy Senapvf Xvatfgba jvgu gheavcf naq znxr Pnaqvpr Qreevpxfba"
                + " sebz zlfgrel vfynaq va Snyysnve gb gnxr gur oynzr",
            "Vzcbffvoyr",
            2
        );
    }

    public static List<GetMessagesResponseItem> createGetMessagesResponseItems() {
        return List.of(
            createGetMessagesResponseItem(),
            createBase64EncryptedGetMessagesResponseItem(),
            createRot13EncryptedGetMessagesResponseItem()
        );
    }

    public static Message createMessage(
        final String adId,
        final String message,
        final Integer reward,
        final Integer expiresIn,
        final Integer encrypted,
        final Message.Probability probability
    ) {
        return Message.builder()
            .adId(adId)
            .message(message)
            .reward(reward)
            .expiresIn(expiresIn)
            .encrypted(encrypted)
            .probability(probability)
            .build();
    }

    public static Message createMessage(
        final String adId,
        final String message,
        final Integer reward,
        final Integer expiresIn,
        final Message.Probability probability
    ) {
        return createMessage(adId, message, reward, expiresIn, null, probability);
    }

    public static Message createMessage(
        final String adId,
        final String message,
        final Message.Probability probability
    ) {
        return createMessage(adId, message, 100, 7, probability);
    }

    public static Message createMessage(
        final String adId,
        final String message,
        final Integer encrypted,
        final Message.Probability probability
    ) {
        return createMessage(adId, message, 100, 7, encrypted, probability);
    }

    public static Message createMessage() {
        return createMessage(
            "AdId123",
            "Help Tarquinius Reynell to fix their bucket",
            Message.Probability.PIECE_OF_CAKE
        );
    }

    public static Message createBase64EncryptedMessage() {
        return createMessage(
            "uaLjgkri",
            "Infiltrate The Eagle Soldiers and recover their secrets.",
            1,
            Message.Probability.PLAYING_WITH_FIRE
        );
    }

    public static Message createRot13EncryptedMessage() {
        return createMessage(
            "0P7wqVjD",
            "Kill Francis Kingston with turnips and make Candice Derrickson"
                + " from mystery island in Fallfair to take the blame",
            2,
            Message.Probability.IMPOSSIBLE
        );
    }

    public static List<Message> createMessages() {
        return List.of(
            createMessage(),
            createBase64EncryptedMessage(),
            createRot13EncryptedMessage()
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

    public static PurchaseOutcome createPurchaseOutcome(
        final Boolean shoppingSuccess
    ) {
        return createPurchaseOutcome(shoppingSuccess, 100, 3, 2, 42);
    }

    public static PurchaseOutcome createPurchaseOutcome() {
        return createPurchaseOutcome(true);
    }
}
