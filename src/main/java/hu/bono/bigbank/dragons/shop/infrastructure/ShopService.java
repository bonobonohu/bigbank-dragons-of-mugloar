package hu.bono.bigbank.dragons.shop.infrastructure;

import hu.bono.bigbank.dragons.common.domain.GameSession;
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
        final GameSession gameSession
    ) {
        final List<GetShopResponseItem> getShopResponseItems = shopClient.getShop(gameSession.getGameId());
        return getShopResponseItems.stream()
            .map(GetShopResponseItemMapper.MAPPER::getShopResponseItemToShopItem)
            .toList();
    }

    public PurchaseOutcome purchaseItem(
        final GameSession gameSession,
        final ShopItem shopItem
    ) {
        final PostShopBuyItemResponse postShopBuyItemResponse =
            shopClient.postShopBuyItem(gameSession.getGameId(), shopItem.id());
        return PostShopBuyItemResponseMapper.MAPPER
            .postShopBuyItemResponseToPurchaseOutcome(postShopBuyItemResponse);
    }
}
