package hu.bono.bigbank.dragons.shop.domain;

import lombok.Builder;

@Builder
public record ShopItem(
    String id,
    String name,
    Integer cost
) {

    public static final String HEALING_POT_ID = "hpot";
    public static final int HEALING_POT_COST = 50;
    public static final int LEVEL_ONE_ITEM_COST = 100;
    public static final int LEVEL_TWO_ITEM_COST = 300;

    public boolean isHealingPot() {
        return id.equals(HEALING_POT_ID);
    }
}
