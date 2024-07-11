package hu.bono.bigbank.dragons.shop.domain;

import lombok.Builder;

@Builder
public record ShopItem(
    String id,
    String name,
    Integer cost
) {

}
