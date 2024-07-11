package hu.bono.bigbank.dragons.shop.application;

import hu.bono.bigbank.dragons.TestUtils;
import hu.bono.bigbank.dragons.shop.domain.ShopItem;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class GetShopResponseItemMapperTest {

    private final GetShopResponseItemMapper underTest = GetShopResponseItemMapper.MAPPER;

    @Test
    void testGetShopResponseItemToShopItemWhenGetShopResponseItemIsNull() {
        final ShopItem actual = underTest.getShopResponseItemToShopItem(null);
        Assertions.assertThat(actual).isNull();
    }

    @Test
    void testGetShopResponseItemToShopItemWhenGetShopResponseItemIsNotNull() {
        final ShopItem expected = TestUtils.createShopItem(
            "hpot",
            "Healing potion",
            50
        );
        final ShopItem actual = underTest.getShopResponseItemToShopItem(
            TestUtils.createGetShopResponseItem(
                "hpot",
                "Healing potion",
                50
            )
        );
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}
