package hu.bono.bigbank.dragons.common.infrastructure;

import hu.bono.bigbank.dragons.common.domain.GameSession;
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
        GameSession gameSession
    );

    List<Message> getMessages(
        GameSession gameSession
    );

    MissionOutcome goOnMission(
        GameSession gameSession,
        Message message
    );

    List<ShopItem> getShopItems(
        GameSession gameSession
    );

    PurchaseOutcome purchaseItem(
        GameSession gameSession,
        ShopItem shopItem
    );
}
