package hu.bono.bigbank.dragons.shop.domain;

import hu.bono.bigbank.dragons.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class ShopTest {

    private final Shop underTest = new Shop();

    @Test
    void testGetItemsAsSetShouldReturnItemsInASet() {
        final Set<ShopItem> expected = new HashSet<>(TestUtils.SHOP_ITEMS.values());
        underTest.setItems(TestUtils.SHOP_ITEMS);
        final Set<ShopItem> actual = underTest.getItemsAsSet();
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testGetLevelUpItemsCountShouldReturnCorrectCount() {
        final int expected = TestUtils.SHOP_ITEMS.values().size() - 1;
        underTest.setItems(TestUtils.SHOP_ITEMS);
        final int actual = underTest.getLevelUpItemsCount();
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testSetItemsFromACollectionShouldSetItems() {
        final Map<String, ShopItem> expected = TestUtils.SHOP_ITEMS;
        underTest.setItems(TestUtils.SHOP_ITEMS.values());
        final Map<String, ShopItem> actual = underTest.getItems();
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testGetHpotShouldThrowExceptionWhenHPotIsNotPresent() {
        underTest.setItems(
            TestUtils.SHOP_ITEMS.values().stream()
                .filter(item -> !item.isHealingPot())
                .toList()
        );
        Assertions.assertThatThrownBy(underTest::getHPot)
            .isInstanceOf(RuntimeException.class)
            .message().isEqualTo("No Healing potion in Shop");
    }

}
