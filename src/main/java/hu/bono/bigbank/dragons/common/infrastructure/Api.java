package hu.bono.bigbank.dragons.common.infrastructure;

import hu.bono.bigbank.dragons.game.domain.Game;
import hu.bono.bigbank.dragons.investigation.domain.Reputation;
import hu.bono.bigbank.dragons.mission.domain.Message;
import hu.bono.bigbank.dragons.mission.domain.MissionOutcome;
import hu.bono.bigbank.dragons.shop.domain.PurchaseOutcome;
import hu.bono.bigbank.dragons.shop.domain.ShopItem;

import java.util.List;

/**
 * Facade for the API.
 */
public interface Api {

    Game startGame();

    Reputation investigateReputation(
        String gameId
    );

    List<Message> getMessages(
        String gameId
    );

    MissionOutcome goOnMission(
        String gameId,
        String adId
    );

    List<ShopItem> getShopItems(
        String gameId
    );

    PurchaseOutcome purchaseItem(
        String gameId,
        String shopItemId
    );
}
