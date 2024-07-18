package hu.bono.bigbank.dragons.common.infrastructure;

import hu.bono.bigbank.dragons.TestUtils;
import hu.bono.bigbank.dragons.game.domain.Game;
import hu.bono.bigbank.dragons.game.infrastructure.GameService;
import hu.bono.bigbank.dragons.investigation.domain.Reputation;
import hu.bono.bigbank.dragons.investigation.infrastructure.InvestigateService;
import hu.bono.bigbank.dragons.mission.domain.Message;
import hu.bono.bigbank.dragons.mission.domain.MissionOutcome;
import hu.bono.bigbank.dragons.mission.infrastructure.MissionService;
import hu.bono.bigbank.dragons.shop.domain.PurchaseOutcome;
import hu.bono.bigbank.dragons.shop.domain.ShopItem;
import hu.bono.bigbank.dragons.shop.infrastructure.ShopService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

class ApiServiceTest {

    private static final String GAME_ID = "GameId123";
    private static final String AD_ID = "AdId123";
    private static final String SHOP_ITEM_ID = "hpot";

    private final GameService gameService = Mockito.mock(GameService.class);
    private final InvestigateService investigateService = Mockito.mock(InvestigateService.class);
    private final MissionService missionService = Mockito.mock(MissionService.class);
    private final ShopService shopService = Mockito.mock(ShopService.class);
    private final ApiService underTest = new ApiService(gameService, investigateService, missionService, shopService);

    @BeforeEach
    void beforeEach() {
        Mockito.reset(gameService, investigateService, missionService, shopService);
    }

    @Test
    void testStartGame() {
        final Game expected = TestUtils.createGame();
        Mockito.when(gameService.startGame())
            .thenReturn(expected);
        final Game actual = underTest.startGame();
        Assertions.assertThat(actual).isEqualTo(expected);
        Mockito.verify(gameService)
            .startGame();
    }

    @Test
    void testInvestigateReputation() {
        final Reputation expected = TestUtils.createReputation();
        Mockito.when(investigateService.investigateReputation(GAME_ID))
            .thenReturn(expected);
        final Reputation actual = underTest.investigateReputation(GAME_ID);
        Assertions.assertThat(actual).isEqualTo(expected);
        Mockito.verify(investigateService)
            .investigateReputation(GAME_ID);
    }

    @Test
    void testGetMessages() {
        final List<Message> expected = TestUtils.createMessages();
        Mockito.when(missionService.getMessages(GAME_ID))
            .thenReturn(expected);
        final List<Message> actual = underTest.getMessages(GAME_ID);
        Assertions.assertThat(actual).isEqualTo(expected);
        Mockito.verify(missionService)
            .getMessages(GAME_ID);
    }

    @Test
    void testGoOnMission() {
        final MissionOutcome expected = TestUtils.createMissionOutcome();
        Mockito.when(missionService.goOnMission(GAME_ID, AD_ID))
            .thenReturn(expected);
        final MissionOutcome actual = underTest.goOnMission(GAME_ID, AD_ID);
        Assertions.assertThat(actual).isEqualTo(expected);
        Mockito.verify(missionService)
            .goOnMission(GAME_ID, AD_ID);
    }

    @Test
    void testGetShopItems() {
        final List<ShopItem> expected = TestUtils.createShopItems();
        Mockito.when(shopService.getItems(GAME_ID))
            .thenReturn(expected);
        final List<ShopItem> actual = underTest.getShopItems(GAME_ID);
        Assertions.assertThat(actual).isEqualTo(expected);
        Mockito.verify(shopService)
            .getItems(GAME_ID);
    }

    @Test
    void testPurchaseItem() {
        final PurchaseOutcome expected = TestUtils.createPurchaseOutcome();
        Mockito.when(shopService.purchaseItem(GAME_ID, SHOP_ITEM_ID))
            .thenReturn(expected);
        final PurchaseOutcome actual = underTest.purchaseItem(GAME_ID, SHOP_ITEM_ID);
        Assertions.assertThat(actual).isEqualTo(expected);
        Mockito.verify(shopService)
            .purchaseItem(GAME_ID, SHOP_ITEM_ID);
    }
}
