package hu.bono.bigbank.dragons.shop.application;

import hu.bono.bigbank.dragons.shop.domain.ShopItem;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GetShopResponseItemMapper {

    GetShopResponseItemMapper MAPPER = Mappers.getMapper(GetShopResponseItemMapper.class);

    ShopItem getShopResponseItemToShopItem(
        GetShopResponseItem getShopResponseItem
    );
}
