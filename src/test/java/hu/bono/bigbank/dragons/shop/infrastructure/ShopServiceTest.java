package hu.bono.bigbank.dragons.shop.infrastructure;

import hu.bono.bigbank.dragons.TestUtils;
import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.shop.application.GetShopResponseItem;
import hu.bono.bigbank.dragons.shop.application.PostShopBuyItemResponse;
import hu.bono.bigbank.dragons.shop.application.ShopClient;
import hu.bono.bigbank.dragons.shop.domain.PurchaseOutcome;
import hu.bono.bigbank.dragons.shop.domain.ShopItem;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.List;

class ShopServiceTest {

    private static final GameSession GAME_SESSION = TestUtils.createGameSession(Instant.now());
    private static final ShopItem SHOP_ITEM = TestUtils.createShopItem();

    private final ShopClient shopClient = Mockito.mock(ShopClient.class);
    private final ShopService underTest = new ShopService(shopClient);

    @BeforeEach
    void beforeEach() {
        Mockito.reset(shopClient);
    }

    @Test
    void testGetAvailableItems() {
        final List<GetShopResponseItem> getShopResponseItems = TestUtils.createGetShopResponseItems();
        final List<ShopItem> expected = TestUtils.createShopItems();
        Mockito.when(shopClient.getShop(GAME_SESSION.getGameId()))
            .thenReturn(getShopResponseItems);
        final List<ShopItem> actual = underTest.getAvailableItems(GAME_SESSION);
        Assertions.assertThat(actual).isEqualTo(expected);
        Mockito.verify(shopClient)
            .getShop(GAME_SESSION.getGameId());
    }

    @Test
    void testPurchaseItem() {
        final PostShopBuyItemResponse postShopBuyItemResponse =
            TestUtils.createPostShopBuyItemResponse(true, 100, 3, 2, 42);
        final PurchaseOutcome expected =
            TestUtils.createPurchaseOutcome(true, 100, 3, 2, 42);
        Mockito.when(shopClient.postShopBuyItem(GAME_SESSION.getGameId(), SHOP_ITEM.id()))
            .thenReturn(postShopBuyItemResponse);
        final PurchaseOutcome actual = underTest.purchaseItem(GAME_SESSION, SHOP_ITEM);
        Assertions.assertThat(actual).isEqualTo(expected);
        Mockito.verify(shopClient)
            .postShopBuyItem(GAME_SESSION.getGameId(), SHOP_ITEM.id());
    }
}
