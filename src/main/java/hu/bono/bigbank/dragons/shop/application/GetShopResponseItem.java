package hu.bono.bigbank.dragons.shop.application;

import lombok.Builder;

@Builder
public record GetShopResponseItem(
    String id,
    String name,
    Integer cost
) {

}
