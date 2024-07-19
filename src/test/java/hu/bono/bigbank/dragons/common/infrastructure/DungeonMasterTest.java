package hu.bono.bigbank.dragons.common.infrastructure;

import hu.bono.bigbank.dragons.TestUtils;
import hu.bono.bigbank.dragons.common.domain.CharacterSheet;
import hu.bono.bigbank.dragons.common.domain.GameMapper;
import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.common.domain.RestClientClientException;
import hu.bono.bigbank.dragons.game.domain.Game;
import hu.bono.bigbank.dragons.investigation.domain.Reputation;
import hu.bono.bigbank.dragons.mission.domain.Message;
import hu.bono.bigbank.dragons.mission.domain.MissionOutcome;
import hu.bono.bigbank.dragons.shop.domain.PurchaseOutcome;
import hu.bono.bigbank.dragons.shop.domain.Shop;
import hu.bono.bigbank.dragons.shop.domain.ShopItem;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClientResponseException;

import java.nio.charset.Charset;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;

class DungeonMasterTest {

    private static final GameSession GAME_SESSION = TestUtils.createGameSession(Instant.now());

    private final Api api = Mockito.mock(Api.class);
    private final GameMapper gameMapper = Mockito.mock(GameMapper.class);
    private final LogWriter logWriter = Mockito.mock(LogWriter.class);
    private final DungeonMaster underTest = new DungeonMaster(api, gameMapper, logWriter);

    @BeforeEach
    void beforeEach() {
        Mockito.reset(api, gameMapper, logWriter);
    }

    @Test
    void testStartGame() {
        final CharacterSheet characterSheet = TestUtils.createCharacterSheet();
        final Game game = TestUtils.createGame(GAME_SESSION.getGameId());
        final GameSession expected = GAME_SESSION;
        Mockito.when(api.startGame())
            .thenReturn(game);
        Mockito.when(gameMapper.gameToGameSession(game, characterSheet))
            .thenReturn(expected);
        final GameSession actual = underTest.startGame(characterSheet);
        Assertions.assertThat(actual.getGameId()).isEqualTo(expected.getGameId());
        Mockito.verify(api)
            .startGame();
        Mockito.verify(logWriter)
            .log(actual, "startGame", null, actual);
    }

    @Test
    void testLoadShop() {
        final GameSession gameSession = TestUtils.clone(GAME_SESSION);
        final List<ShopItem> shopItems = TestUtils.SHOP_ITEMS.values().stream().toList();
        Mockito.when(api.getShopItems(gameSession.getGameId()))
            .thenReturn(shopItems);
        underTest.loadShop(gameSession);
        Assertions.assertThat(gameSession.getShop().getItems())
            .isEqualTo(Shop.itemsCollectionToMap(shopItems));
        Mockito.verify(api)
            .getShopItems(gameSession.getGameId());
        Mockito.verify(logWriter)
            .log(gameSession, "loadShop", null, shopItems);
    }

    @Test
    void testPurchaseItemWhenNotEnoughMoney() {
        final GameSession gameSession = TestUtils.clone(GAME_SESSION);
        gameSession.getCharacterSheet().setGold(0);
        underTest.purchaseItem(gameSession, TestUtils.HEALING_POT);
        Mockito.verify(api, Mockito.times(0))
            .purchaseItem(gameSession.getGameId(), TestUtils.HEALING_POT.id());
        Mockito.verify(logWriter, Mockito.times(0))
            .log(any(), any(), any(), any());
    }

    @Test
    void testPurchaseItemWhenPurchaseFailure() {
        final GameSession gameSession = TestUtils.clone(GAME_SESSION);
        gameSession.getCharacterSheet().setGold(10_000_000);
        final ShopItem shopItem = TestUtils.HEALING_POT;
        final PurchaseOutcome purchaseOutcome = TestUtils.createPurchaseOutcome(false);
        Mockito.when(api.purchaseItem(gameSession.getGameId(), shopItem.id()))
            .thenReturn(purchaseOutcome);
        underTest.purchaseItem(gameSession, shopItem);
        Assertions.assertThat(gameSession.getTurn())
            .isEqualTo(purchaseOutcome.turn());
        Assertions.assertThat(gameSession.getMyBook().getTurn())
            .isEqualTo(5);
        Mockito.verify(api, Mockito.times(5))
            .purchaseItem(gameSession.getGameId(), shopItem.id());
        Mockito.verify(logWriter, Mockito.times(5))
            .log(gameSession, "purchaseItemAttempt", shopItem, purchaseOutcome);
        Mockito.verify(logWriter, Mockito.times(0))
            .log(gameSession, "purchaseItemSuccess", shopItem, purchaseOutcome);
        Mockito.verify(logWriter)
            .log(gameSession, "purchaseItemFailure", shopItem, purchaseOutcome);
    }

    @Test
    void testPurchaseItemWhenHealingPotPurchaseSuccess() {
        final GameSession gameSession = TestUtils.clone(GAME_SESSION);
        gameSession.getCharacterSheet().setGold(10_000_000);
        gameSession.getCharacterSheet().getMyBook().setGold(10_000_000);
        gameSession.getShop().setItems(TestUtils.SHOP_ITEMS);
        final ShopItem shopItem = TestUtils.HEALING_POT;
        final PurchaseOutcome purchaseOutcome = TestUtils.createPurchaseOutcome(true);
        Mockito.when(api.purchaseItem(gameSession.getGameId(), shopItem.id()))
            .thenReturn(purchaseOutcome);
        underTest.purchaseItem(gameSession, shopItem);
        Assertions.assertThat(gameSession.getTurn())
            .isEqualTo(purchaseOutcome.turn());
        Assertions.assertThat(gameSession.getMyBook().getTurn())
            .isEqualTo(1);
        Assertions.assertThat(gameSession.getCharacterSheet().getGold())
            .isEqualTo(purchaseOutcome.gold());
        Assertions.assertThat(gameSession.getCharacterSheet().getLives())
            .isEqualTo(purchaseOutcome.lives());
        Assertions.assertThat(gameSession.getCharacterSheet().getLevel())
            .isEqualTo(purchaseOutcome.level());
        Assertions.assertThat(gameSession.getCharacterSheet().getMyBook().getGold())
            .isEqualTo(10_000_000 - shopItem.cost());
        Assertions.assertThat(gameSession.getCharacterSheet().getMyBook().getLives())
            .isEqualTo(3 + shopItem.getLivesGain());
        Assertions.assertThat(gameSession.getCharacterSheet().getMyBook().getLevel())
            .isEqualTo(0 + shopItem.getLevelGain());
        Assertions.assertThat(gameSession.getPurchasedItems())
            .doesNotContain(shopItem);
        Mockito.verify(api, Mockito.times(1))
            .purchaseItem(gameSession.getGameId(), shopItem.id());
        Mockito.verify(logWriter, Mockito.times(1))
            .log(gameSession, "purchaseItemAttempt", shopItem, purchaseOutcome);
        Mockito.verify(logWriter)
            .log(gameSession, "purchaseItemSuccess", shopItem, purchaseOutcome);
        Mockito.verify(logWriter, Mockito.times(0))
            .log(gameSession, "purchaseItemFailure", shopItem, purchaseOutcome);
    }

    @Test
    void testPurchaseItemWhenClawSharpeningPurchaseSuccess() {
        final GameSession gameSession = TestUtils.clone(GAME_SESSION);
        gameSession.getCharacterSheet().setGold(10_000_000);
        gameSession.getCharacterSheet().getMyBook().setGold(10_000_000);
        gameSession.getShop().setItems(TestUtils.SHOP_ITEMS);
        final ShopItem shopItem = TestUtils.CLAW_SHARPENING;
        final PurchaseOutcome purchaseOutcome = TestUtils.createPurchaseOutcome(true);
        Mockito.when(api.purchaseItem(gameSession.getGameId(), shopItem.id()))
            .thenReturn(purchaseOutcome);
        underTest.purchaseItem(gameSession, shopItem);
        Assertions.assertThat(gameSession.getTurn())
            .isEqualTo(purchaseOutcome.turn());
        Assertions.assertThat(gameSession.getMyBook().getTurn())
            .isEqualTo(1);
        Assertions.assertThat(gameSession.getCharacterSheet().getGold())
            .isEqualTo(purchaseOutcome.gold());
        Assertions.assertThat(gameSession.getCharacterSheet().getLives())
            .isEqualTo(purchaseOutcome.lives());
        Assertions.assertThat(gameSession.getCharacterSheet().getLevel())
            .isEqualTo(purchaseOutcome.level());
        Assertions.assertThat(gameSession.getCharacterSheet().getMyBook().getGold())
            .isEqualTo(10_000_000 - shopItem.cost());
        Assertions.assertThat(gameSession.getCharacterSheet().getMyBook().getLives())
            .isEqualTo(3 + shopItem.getLivesGain());
        Assertions.assertThat(gameSession.getCharacterSheet().getMyBook().getLevel())
            .isEqualTo(0 + shopItem.getLevelGain());
        Assertions.assertThat(gameSession.getPurchasedItems())
            .contains(shopItem);
        Mockito.verify(api, Mockito.times(1))
            .purchaseItem(gameSession.getGameId(), shopItem.id());
        Mockito.verify(logWriter, Mockito.times(1))
            .log(gameSession, "purchaseItemAttempt", shopItem, purchaseOutcome);
        Mockito.verify(logWriter)
            .log(gameSession, "purchaseItemSuccess", shopItem, purchaseOutcome);
        Mockito.verify(logWriter, Mockito.times(0))
            .log(gameSession, "purchaseItemFailure", shopItem, purchaseOutcome);
    }

    @Test
    void testPurchaseItemWhenClawSharpeningPurchaseSuccessPurchasedItemsErased() {
        final GameSession gameSession = TestUtils.clone(GAME_SESSION);
        gameSession.getCharacterSheet().setGold(10_000_000);
        gameSession.getShop().setItems(TestUtils.SHOP_ITEMS);
        final ShopItem shopItem = TestUtils.CLAW_SHARPENING;
        final Set<ShopItem> purchasedItems = new HashSet<>(TestUtils.SHOP_ITEMS.values());
        purchasedItems.remove(TestUtils.HEALING_POT);
        purchasedItems.remove(shopItem);
        gameSession.setPurchasedItems(purchasedItems);
        final PurchaseOutcome purchaseOutcome = TestUtils.createPurchaseOutcome(true);
        Mockito.when(api.purchaseItem(gameSession.getGameId(), shopItem.id()))
            .thenReturn(purchaseOutcome);
        underTest.purchaseItem(gameSession, shopItem);
        Assertions.assertThat(gameSession.getPurchasedItems())
            .isEmpty();
    }

    @Test
    void testRefreshMessages() {
        final GameSession gameSession = TestUtils.clone(GAME_SESSION);
        final List<Message> messages = TestUtils.createMessages();
        Mockito.when(api.getMessages(gameSession.getGameId()))
            .thenReturn(messages);
        underTest.refreshMessages(gameSession);
        Assertions.assertThat(gameSession.getMessages())
            .isEqualTo(new HashSet<>(messages));
        Mockito.verify(api, Mockito.times(2))
            .getMessages(gameSession.getGameId());
        Mockito.verify(logWriter)
            .log(gameSession, "fakeMessages", null, messages);
        Mockito.verify(logWriter)
            .log(gameSession, "refreshMessages", null, messages);
    }

    @Test
    void testGoOnMissionWhenMessageWasNotFound() {
        final GameSession gameSession = TestUtils.clone(GAME_SESSION);
        final Message message = TestUtils.createMessage();
        Mockito.when(api.goOnMission(gameSession.getGameId(), message.adId()))
            .thenThrow(
                new RestClientClientException(
                    new RestClientResponseException(
                        "No ad by this ID exists",
                        HttpStatusCode.valueOf(400),
                        "No ad by this ID exists",
                        null,
                        null,
                        Charset.defaultCharset()
                    )
                )
            );
        underTest.goOnMission(gameSession, message);
        Mockito.verify(api, Mockito.times(5))
            .goOnMission(gameSession.getGameId(), message.adId());
        Mockito.verify(logWriter, Mockito.times(0))
            .log(any(), any(), any(), any());
    }

    @Test
    void testGoOnMissionWhenMissionDie() {
        final GameSession gameSession = TestUtils.clone(GAME_SESSION);
        gameSession.getCharacterSheet().getMyBook().setGold(10_000_000);
        final Message message = TestUtils.createMessage();
        final MissionOutcome missionOutcome = TestUtils.createMissionOutcome(
            false,
            0,
            1,
            1,
            1
        );
        Mockito.when(api.goOnMission(gameSession.getGameId(), message.adId()))
            .thenReturn(missionOutcome);
        underTest.goOnMission(gameSession, message);
        Mockito.verify(api, Mockito.times(5))
            .goOnMission(gameSession.getGameId(), message.adId());
        Assertions.assertThat(gameSession.getCharacterSheet().getLives())
            .isEqualTo(missionOutcome.lives());
        Assertions.assertThat(gameSession.getCharacterSheet().getGold())
            .isEqualTo(missionOutcome.gold());
        Assertions.assertThat(gameSession.getCharacterSheet().getScore())
            .isEqualTo(missionOutcome.score());
        Assertions.assertThat(gameSession.getCharacterSheet().getHighScore())
            .isEqualTo(missionOutcome.highScore());
        Assertions.assertThat(gameSession.getCharacterSheet().getMyBook().getLives())
            .isEqualTo(
                3
                    + (missionOutcome.success() ? 0 : -1)
            );
        Assertions.assertThat(gameSession.getCharacterSheet().getMyBook().getGold())
            .isEqualTo(
                10_000_000
                    + (missionOutcome.success() ? message.reward() : 0)
            );
        Assertions.assertThat(gameSession.getCharacterSheet().getMyBook().getScore())
            .isEqualTo(
                0
                    + (missionOutcome.success() ? message.reward() : 0)
            );
        Assertions.assertThat(gameSession.getTurn())
            .isEqualTo(missionOutcome.turn());
        Assertions.assertThat(gameSession.getMyBook().getTurn())
            .isEqualTo(1);
        Mockito.verify(logWriter, Mockito.times(5))
            .log(gameSession, "goOnMissionAttempt", message, missionOutcome);
        Mockito.verify(logWriter, Mockito.times(0))
            .log(gameSession, "goOnMissionSuccess", message, missionOutcome);
        Mockito.verify(logWriter, Mockito.times(0))
            .log(gameSession, "goOnMissionFailure", message, missionOutcome);
        Mockito.verify(logWriter)
            .log(gameSession, "goOnMissionDie", message, missionOutcome);
        Mockito.verify(logWriter)
            .log(gameSession, "dieDieDie", missionOutcome, gameSession.getCharacterSheet());
    }

    @Test
    void testGoOnMissionWhenMissionFailure() {
        final GameSession gameSession = TestUtils.clone(GAME_SESSION);
        gameSession.getCharacterSheet().getMyBook().setGold(10_000_000);
        final Message message = TestUtils.createMessage();
        final MissionOutcome missionOutcome = TestUtils.createMissionOutcome(
            false,
            2,
            1,
            1,
            1
        );
        Mockito.when(api.goOnMission(gameSession.getGameId(), message.adId()))
            .thenReturn(missionOutcome);
        underTest.goOnMission(gameSession, message);
        Mockito.verify(api, Mockito.times(5))
            .goOnMission(gameSession.getGameId(), message.adId());
        Assertions.assertThat(gameSession.getCharacterSheet().getLives())
            .isEqualTo(missionOutcome.lives());
        Assertions.assertThat(gameSession.getCharacterSheet().getGold())
            .isEqualTo(missionOutcome.gold());
        Assertions.assertThat(gameSession.getCharacterSheet().getScore())
            .isEqualTo(missionOutcome.score());
        Assertions.assertThat(gameSession.getCharacterSheet().getHighScore())
            .isEqualTo(missionOutcome.highScore());
        Assertions.assertThat(gameSession.getCharacterSheet().getMyBook().getLives())
            .isEqualTo(
                3
                    + (missionOutcome.success() ? 0 : -1)
            );
        Assertions.assertThat(gameSession.getCharacterSheet().getMyBook().getGold())
            .isEqualTo(
                10_000_000
                    + (missionOutcome.success() ? message.reward() : 0)
            );
        Assertions.assertThat(gameSession.getCharacterSheet().getMyBook().getScore())
            .isEqualTo(
                0
                    + (missionOutcome.success() ? message.reward() : 0)
            );
        Assertions.assertThat(gameSession.getTurn())
            .isEqualTo(missionOutcome.turn());
        Assertions.assertThat(gameSession.getMyBook().getTurn())
            .isEqualTo(1);
        Mockito.verify(logWriter, Mockito.times(5))
            .log(gameSession, "goOnMissionAttempt", message, missionOutcome);
        Mockito.verify(logWriter, Mockito.times(0))
            .log(gameSession, "goOnMissionSuccess", message, missionOutcome);
        Mockito.verify(logWriter)
            .log(gameSession, "goOnMissionFailure", message, missionOutcome);
        Mockito.verify(logWriter, Mockito.times(0))
            .log(gameSession, "goOnMissionDie", message, missionOutcome);
        Mockito.verify(logWriter, Mockito.times(0))
            .log(gameSession, "dieDieDie", missionOutcome, gameSession.getCharacterSheet());
    }

    @Test
    void testGoOnMissionWhenMissionSuccess() {
        final GameSession gameSession = TestUtils.clone(GAME_SESSION);
        gameSession.getCharacterSheet().getMyBook().setGold(10_000_000);
        final Message message = TestUtils.createMessage();
        final MissionOutcome missionOutcome = TestUtils.createMissionOutcome(
            true,
            3,
            1,
            1,
            1
        );
        Mockito.when(api.goOnMission(gameSession.getGameId(), message.adId()))
            .thenReturn(missionOutcome);
        underTest.goOnMission(gameSession, message);
        Mockito.verify(api, Mockito.times(5))
            .goOnMission(gameSession.getGameId(), message.adId());
        Assertions.assertThat(gameSession.getCharacterSheet().getLives())
            .isEqualTo(missionOutcome.lives());
        Assertions.assertThat(gameSession.getCharacterSheet().getGold())
            .isEqualTo(missionOutcome.gold());
        Assertions.assertThat(gameSession.getCharacterSheet().getScore())
            .isEqualTo(missionOutcome.score());
        Assertions.assertThat(gameSession.getCharacterSheet().getHighScore())
            .isEqualTo(missionOutcome.highScore());
        Assertions.assertThat(gameSession.getCharacterSheet().getMyBook().getLives())
            .isEqualTo(
                3
                    + (missionOutcome.success() ? 0 : -1)
            );
        Assertions.assertThat(gameSession.getCharacterSheet().getMyBook().getGold())
            .isEqualTo(
                10_000_000
                    + (missionOutcome.success() ? message.reward() : 0)
            );
        Assertions.assertThat(gameSession.getCharacterSheet().getMyBook().getScore())
            .isEqualTo(
                0
                    + (missionOutcome.success() ? message.reward() : 0)
            );
        Assertions.assertThat(gameSession.getTurn())
            .isEqualTo(missionOutcome.turn());
        Assertions.assertThat(gameSession.getMyBook().getTurn())
            .isEqualTo(1);
        Mockito.verify(logWriter, Mockito.times(5))
            .log(gameSession, "goOnMissionAttempt", message, missionOutcome);
        Mockito.verify(logWriter)
            .log(gameSession, "goOnMissionSuccess", message, missionOutcome);
        Mockito.verify(logWriter, Mockito.times(0))
            .log(gameSession, "goOnMissionFailure", message, missionOutcome);
        Mockito.verify(logWriter, Mockito.times(0))
            .log(gameSession, "goOnMissionDie", message, missionOutcome);
        Mockito.verify(logWriter, Mockito.times(0))
            .log(gameSession, "dieDieDie", missionOutcome, gameSession.getCharacterSheet());
    }

    @Test
    void testInvestigateReputation() {
        final GameSession gameSession = TestUtils.clone(GAME_SESSION);
        final Reputation reputation = TestUtils.createReputation();
        Mockito.when(api.investigateReputation(gameSession.getGameId()))
            .thenReturn(reputation);
        underTest.investigateReputation(gameSession);
        Assertions.assertThat(gameSession.getCharacterSheet().getReputation())
            .isEqualTo(reputation);
        Mockito.verify(api, Mockito.times(2))
            .investigateReputation(gameSession.getGameId());
        Mockito.verify(logWriter)
            .log(gameSession, "fakeReputation", null, reputation);
        Mockito.verify(logWriter)
            .log(gameSession, "investigateReputation", null, reputation);
    }
}
