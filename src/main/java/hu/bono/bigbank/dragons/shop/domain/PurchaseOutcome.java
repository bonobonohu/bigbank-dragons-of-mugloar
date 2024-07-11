package hu.bono.bigbank.dragons.shop.domain;

import lombok.Builder;

@Builder
public record PurchaseOutcome(
    Boolean shoppingSuccess,
    Integer gold,
    Integer lives,
    Integer level,
    Integer turn
) {

}
