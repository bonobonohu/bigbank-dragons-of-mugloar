package hu.bono.bigbank.dragons.shop.infrastructure;

import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.common.infrastructure.LogWriter;
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
    private final LogWriter logWriter;

    public List<ShopItem> getAvailableItems(GameSession gameSession) {
        final List<GetShopResponseItem> getShopResponseItems =
            shopClient.getShop(gameSession.getGameId());
        final List<ShopItem> shopItems = getShopResponseItems.stream()
            .map(GetShopResponseItemMapper.MAPPER::getShopResponseItemToShopItem)
            .toList();
        logWriter.log(
            gameSession,
            "getAvailableItems",
            "Get available items",
            getShopResponseItems
        );
        return shopItems;
    }

    public PurchaseOutcome purchaseItem(GameSession gameSession, ShopItem shopItem) {
        final PostShopBuyItemResponse postShopBuyItemResponse =
            shopClient.postShopBuyItem(gameSession.getGameId(), shopItem.id());
        final PurchaseOutcome purchaseOutcome = PostShopBuyItemResponseMapper.MAPPER
            .postShopBuyItemResponseToPurchaseOutcome(postShopBuyItemResponse);
        logWriter.log(
            gameSession,
            "purchaseItem",
            "Purchase item",
            postShopBuyItemResponse
        );
        return purchaseOutcome;
    }
}
