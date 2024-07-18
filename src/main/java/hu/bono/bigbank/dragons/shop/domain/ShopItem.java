package hu.bono.bigbank.dragons.shop.domain;

import lombok.Builder;

import java.util.Set;

@Builder
public record ShopItem(
    String id,
    String name,
    Integer cost
) {

    public static final String HEALING_POT_ID = "hpot";
    public static final int LEVEL_ONE_ITEM_COST = 100;
    public static final int LEVEL_TWO_ITEM_COST = 300;
    public static final Set<Integer> LEVEL_UP_ITEM_COSTS = Set.of(LEVEL_ONE_ITEM_COST, LEVEL_TWO_ITEM_COST);

    public boolean isHealingPot() {
        return id.equals(HEALING_POT_ID);
    }

    public int getLivesGain() {
        return isHealingPot() ? 1 : 0;
    }

    public int getLevelGain() {
        return switch (cost) {
            case LEVEL_ONE_ITEM_COST -> 1;
            case LEVEL_TWO_ITEM_COST -> 2;
            default -> 0;
        };
    }
}
