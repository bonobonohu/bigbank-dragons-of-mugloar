package hu.bono.bigbank.dragons.shop.domain;

import lombok.Builder;

@Builder
public record ShopItem(
    String id,
    String name,
    Integer cost
) {

    public static final String HEALING_POT = "hpot";

    public boolean isHealingPot() {
        return id.equals(HEALING_POT);
    }
}
