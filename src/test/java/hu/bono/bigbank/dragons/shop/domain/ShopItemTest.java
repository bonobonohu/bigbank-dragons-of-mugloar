package hu.bono.bigbank.dragons.shop.domain;

import hu.bono.bigbank.dragons.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class ShopItemTest {

    @ParameterizedTest
    @MethodSource("isHealingPot")
    void testIsHealingPot(
        final ShopItem shopItem,
        final boolean expected
    ) {
        final boolean actual = shopItem.isHealingPot();
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> isHealingPot() {
        return Stream.of(
            Arguments.of(
                TestUtils.HEALING_POT,
                true),
            Arguments.of(
                TestUtils.SHOP_ITEMS.get("cs"),
                false)
        );
    }

    @ParameterizedTest
    @MethodSource("getLivesGain")
    void testGetLivesGain(
        final ShopItem shopItem,
        final int expected
    ) {
        final int actual = shopItem.getLivesGain();
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> getLivesGain() {
        return Stream.of(
            Arguments.of(
                TestUtils.HEALING_POT,
                1),
            Arguments.of(
                TestUtils.SHOP_ITEMS.get("cs"),
                0)
        );
    }

    @ParameterizedTest
    @MethodSource("getLevelGain")
    void testGetLevelGain(
        final ShopItem shopItem,
        final int expected
    ) {
        final int actual = shopItem.getLevelGain();
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> getLevelGain() {
        return Stream.of(
            Arguments.of(
                TestUtils.HEALING_POT,
                0),
            Arguments.of(
                TestUtils.SHOP_ITEMS.get("cs"),
                1),
            Arguments.of(
                TestUtils.SHOP_ITEMS.get("rf"),
                2)
        );
    }
}
