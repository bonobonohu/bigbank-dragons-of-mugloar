package hu.bono.bigbank.dragons.shop.application;

import hu.bono.bigbank.dragons.TestUtils;
import hu.bono.bigbank.dragons.shop.domain.PurchaseOutcome;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PostShopBuyItemResponseMapperTest {

    private final PostShopBuyItemResponseMapper underTest = PostShopBuyItemResponseMapper.MAPPER;

    @Test
    void testPostShopBuyItemResponseToPurchaseOutcomeWhenPostShopBuyItemResponseIsNull() {
        final PurchaseOutcome actual = underTest.postShopBuyItemResponseToPurchaseOutcome(null);
        Assertions.assertThat(actual).isNull();
    }

    @Test
    void testPostShopBuyItemResponseToPurchaseOutcomeWhenPostShopBuyItemResponseIsNotNull() {
        final PurchaseOutcome expected = TestUtils.createPurchaseOutcome(
            true,
            100,
            3,
            2,
            42
        );
        final PurchaseOutcome actual = underTest.postShopBuyItemResponseToPurchaseOutcome(
            TestUtils.createPostShopBuyItemResponse(
                true,
                100,
                3,
                2,
                42
            )
        );
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}
