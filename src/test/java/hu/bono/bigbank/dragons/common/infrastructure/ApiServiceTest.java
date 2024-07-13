package hu.bono.bigbank.dragons.common.infrastructure;

import hu.bono.bigbank.dragons.TestUtils;
import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.game.domain.Game;
import hu.bono.bigbank.dragons.game.infrastructure.GameService;
import hu.bono.bigbank.dragons.investigation.domain.Reputation;
import hu.bono.bigbank.dragons.investigation.infrastructure.InvestigateService;
import hu.bono.bigbank.dragons.message.domain.Message;
import hu.bono.bigbank.dragons.message.domain.MissionOutcome;
import hu.bono.bigbank.dragons.message.infrastructure.MessageService;
import hu.bono.bigbank.dragons.shop.domain.PurchaseOutcome;
import hu.bono.bigbank.dragons.shop.domain.ShopItem;
import hu.bono.bigbank.dragons.shop.infrastructure.ShopService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.List;

class ApiServiceTest {

    private static final GameSession GAME_SESSION = TestUtils.createGameSession(Instant.now());
    private static final Message MESSAGE = TestUtils.createMessage();
    private static final ShopItem SHOP_ITEM = TestUtils.createShopItem();

    private final GameService gameService = Mockito.mock(GameService.class);
    private final InvestigateService investigateService = Mockito.mock(InvestigateService.class);
    private final MessageService messageService = Mockito.mock(MessageService.class);
    private final ShopService shopService = Mockito.mock(ShopService.class);
    private final ApiService underTest = new ApiService(gameService, investigateService, messageService, shopService);

    @BeforeEach
    void beforeEach() {
        Mockito.reset(gameService, investigateService, messageService, shopService);
    }

    @Test
    void testGameStart() {
        final Game expected = TestUtils.createGame();
        Mockito.when(gameService.gameStart())
            .thenReturn(expected);
        final Game actual = underTest.gameStart();
        Assertions.assertThat(actual).isEqualTo(expected);
        Mockito.verify(gameService)
            .gameStart();
    }

    @Test
    void testInvestigateReputation() {
        final Reputation expected = TestUtils.createReputation();
        Mockito.when(investigateService.investigateReputation(GAME_SESSION))
            .thenReturn(expected);
        final Reputation actual = underTest.investigateReputation(GAME_SESSION);
        Assertions.assertThat(actual).isEqualTo(expected);
        Mockito.verify(investigateService)
            .investigateReputation(GAME_SESSION);
    }

    @Test
    void testGetAllMessages() {
        final List<Message> expected = TestUtils.createMessages();
        Mockito.when(messageService.getAllMessages(GAME_SESSION))
            .thenReturn(expected);
        final List<Message> actual = underTest.getAllMessages(GAME_SESSION);
        Assertions.assertThat(actual).isEqualTo(expected);
        Mockito.verify(messageService)
            .getAllMessages(GAME_SESSION);
    }

    @Test
    void testSolveAd() {
        final MissionOutcome expected = TestUtils.createMissionOutcome();
        Mockito.when(messageService.solveAd(GAME_SESSION, MESSAGE))
            .thenReturn(expected);
        final MissionOutcome actual = underTest.solveAd(GAME_SESSION, MESSAGE);
        Assertions.assertThat(actual).isEqualTo(expected);
        Mockito.verify(messageService)
            .solveAd(GAME_SESSION, MESSAGE);
    }

    @Test
    void testGetAvailableItems() {
        final List<ShopItem> expected = TestUtils.createShopItems();
        Mockito.when(shopService.getAvailableItems(GAME_SESSION))
            .thenReturn(expected);
        final List<ShopItem> actual = underTest.getAvailableItems(GAME_SESSION);
        Assertions.assertThat(actual).isEqualTo(expected);
        Mockito.verify(shopService)
            .getAvailableItems(GAME_SESSION);
    }

    @Test
    void testPurchaseItem() {
        final PurchaseOutcome expected = TestUtils.createPurchaseOutcome();
        Mockito.when(shopService.purchaseItem(GAME_SESSION, SHOP_ITEM))
            .thenReturn(expected);
        final PurchaseOutcome actual = underTest.purchaseItem(GAME_SESSION, SHOP_ITEM);
        Assertions.assertThat(actual).isEqualTo(expected);
        Mockito.verify(shopService)
            .purchaseItem(GAME_SESSION, SHOP_ITEM);
    }
}
