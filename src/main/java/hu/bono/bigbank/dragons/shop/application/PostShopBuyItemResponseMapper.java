package hu.bono.bigbank.dragons.shop.application;

import hu.bono.bigbank.dragons.shop.domain.PurchaseOutcome;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostShopBuyItemResponseMapper {

    PostShopBuyItemResponseMapper MAPPER = Mappers.getMapper(PostShopBuyItemResponseMapper.class);

    PurchaseOutcome postShopBuyItemResponseToPurchaseOutcome(
        PostShopBuyItemResponse postShopBuyItemResponse
    );
}
