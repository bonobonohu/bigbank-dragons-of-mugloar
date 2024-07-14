package hu.bono.bigbank.dragons.common.infrastructure;

import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.game.domain.Game;
import hu.bono.bigbank.dragons.game.infrastructure.GameService;
import hu.bono.bigbank.dragons.investigation.domain.Reputation;
import hu.bono.bigbank.dragons.investigation.infrastructure.InvestigateService;
import hu.bono.bigbank.dragons.mission.domain.Message;
import hu.bono.bigbank.dragons.mission.domain.MissionOutcome;
import hu.bono.bigbank.dragons.mission.infrastructure.MissionService;
import hu.bono.bigbank.dragons.shop.domain.PurchaseOutcome;
import hu.bono.bigbank.dragons.shop.domain.ShopItem;
import hu.bono.bigbank.dragons.shop.infrastructure.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ApiService implements Api {

    private final GameService gameService;
    private final InvestigateService investigateService;
    private final MissionService missionService;
    private final ShopService shopService;

    @Override
    public Game startGame() {
        return gameService.startGame();
    }

    @Override
    public Reputation investigateReputation(
        final GameSession gameSession
    ) {
        return investigateService.investigateReputation(gameSession);
    }

    @Override
    public List<Message> getMessages(
        final GameSession gameSession
    ) {
        return missionService.getMessages(gameSession);
    }

    @Override
    public MissionOutcome goOnMission(
        final GameSession gameSession,
        final Message message
    ) {
        return missionService.goOnMission(gameSession, message);
    }

    @Override
    public List<ShopItem> getShopItems(
        final GameSession gameSession
    ) {
        return shopService.getItems(gameSession);
    }

    @Override
    public PurchaseOutcome purchaseItem(
        final GameSession gameSession,
        final ShopItem shopItem
    ) {
        return shopService.purchaseItem(gameSession, shopItem);
    }
}
