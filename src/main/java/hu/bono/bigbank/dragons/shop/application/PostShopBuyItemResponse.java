package hu.bono.bigbank.dragons.shop.application;

import lombok.Builder;

@Builder
public record PostShopBuyItemResponse(
    Boolean shoppingSuccess,
    Integer gold,
    Integer lives,
    Integer level,
    Integer turn
) {

}
