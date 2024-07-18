package hu.bono.bigbank.dragons.shop.infrastructure;

import hu.bono.bigbank.dragons.TestUtils;
import hu.bono.bigbank.dragons.shop.application.GetShopResponseItem;
import hu.bono.bigbank.dragons.shop.application.PostShopBuyItemResponse;
import hu.bono.bigbank.dragons.shop.application.ShopClient;
import hu.bono.bigbank.dragons.shop.domain.PurchaseOutcome;
import hu.bono.bigbank.dragons.shop.domain.ShopItem;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

class ShopServiceTest {

    private static final String GAME_ID = "GameId123";
    private static final String SHOP_ITEM_ID = "hpot";

    private final ShopClient shopClient = Mockito.mock(ShopClient.class);
    private final ShopService underTest = new ShopService(shopClient);

    @BeforeEach
    void beforeEach() {
        Mockito.reset(shopClient);
    }

    @Test
    void testGetItems() {
        final List<GetShopResponseItem> getShopResponseItems = TestUtils.createGetShopResponseItems();
        final List<ShopItem> expected = TestUtils.createShopItems();
        Mockito.when(shopClient.getShop(GAME_ID))
            .thenReturn(getShopResponseItems);
        final List<ShopItem> actual = underTest.getItems(GAME_ID);
        Assertions.assertThat(actual).isEqualTo(expected);
        Mockito.verify(shopClient)
            .getShop(GAME_ID);
    }

    @Test
    void testPurchaseItem() {
        final PostShopBuyItemResponse postShopBuyItemResponse =
            TestUtils.createPostShopBuyItemResponse(true, 100, 3, 2, 42);
        final PurchaseOutcome expected =
            TestUtils.createPurchaseOutcome(true, 100, 3, 2, 42);
        Mockito.when(shopClient.postShopBuyItem(GAME_ID, SHOP_ITEM_ID))
            .thenReturn(postShopBuyItemResponse);
        final PurchaseOutcome actual = underTest.purchaseItem(GAME_ID, SHOP_ITEM_ID);
        Assertions.assertThat(actual).isEqualTo(expected);
        Mockito.verify(shopClient)
            .postShopBuyItem(GAME_ID, SHOP_ITEM_ID);
    }
}
