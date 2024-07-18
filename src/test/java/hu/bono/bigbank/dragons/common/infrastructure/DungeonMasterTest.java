package hu.bono.bigbank.dragons.common.infrastructure;

import hu.bono.bigbank.dragons.TestUtils;
import hu.bono.bigbank.dragons.common.domain.CharacterSheet;
import hu.bono.bigbank.dragons.common.domain.GameMapper;
import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.game.domain.Game;
import hu.bono.bigbank.dragons.investigation.domain.Reputation;
import hu.bono.bigbank.dragons.mission.domain.Message;
import hu.bono.bigbank.dragons.shop.domain.Shop;
import hu.bono.bigbank.dragons.shop.domain.ShopItem;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;

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

    }

    @Test
    void testPurchaseItemWhenPurchaseFailure() {

    }

    @Test
    void testPurchaseItemWhenPurchaseSuccess() {

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
            .log(gameSession, "refreshMessages", null, messages);
    }

    @Test
    void testGoOnMissionWhenMissionWasNotFound() {

    }

    @Test
    void testGoOnMissionWhenMissionDie() {

    }

    @Test
    void testGoOnMissionWhenMissionFailure() {

    }

    @Test
    void testGoOnMissionWhenMissionSuccess() {

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
            .log(gameSession, "investigateReputation", null, reputation);
    }
}
