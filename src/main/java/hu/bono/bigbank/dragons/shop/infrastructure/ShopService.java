package hu.bono.bigbank.dragons.shop.infrastructure;

import hu.bono.bigbank.dragons.shop.application.*;
import hu.bono.bigbank.dragons.shop.domain.PurchaseOutcome;
import hu.bono.bigbank.dragons.shop.domain.ShopItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ShopService {

    private final ShopClient shopClient;

    public List<ShopItem> getItems(
        final String gameId
    ) {
        final List<GetShopResponseItem> getShopResponseItems = shopClient.getShop(gameId);
        return getShopResponseItems.stream()
            .map(GetShopResponseItemMapper.MAPPER::getShopResponseItemToShopItem)
            .toList();
    }

    public PurchaseOutcome purchaseItem(
        final String gameId,
        final String shopItemId
    ) {
        final PostShopBuyItemResponse postShopBuyItemResponse =
            shopClient.postShopBuyItem(gameId, shopItemId);
        return PostShopBuyItemResponseMapper.MAPPER
            .postShopBuyItemResponseToPurchaseOutcome(postShopBuyItemResponse);
    }
}
