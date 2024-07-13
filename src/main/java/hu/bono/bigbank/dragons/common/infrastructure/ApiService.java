package hu.bono.bigbank.dragons.common.infrastructure;

import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.game.domain.Game;
import hu.bono.bigbank.dragons.game.infrastructure.GameService;
import hu.bono.bigbank.dragons.investigation.domain.Reputation;
import hu.bono.bigbank.dragons.investigation.infrastructure.InvestigateService;
import hu.bono.bigbank.dragons.message.domain.Message;
import hu.bono.bigbank.dragons.message.domain.MissionOutcome;
import hu.bono.bigbank.dragons.message.infrastructure.MessageService;
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
    private final MessageService messageService;
    private final ShopService shopService;

    @Override
    public Game gameStart() {
        return gameService.gameStart();
    }

    @Override
    public Reputation investigateReputation(
        final GameSession gameSession
    ) {
        return investigateService.investigateReputation(gameSession);
    }

    @Override
    public List<Message> getAllMessages(
        final GameSession gameSession
    ) {
        return messageService.getAllMessages(gameSession);
    }

    @Override
    public MissionOutcome solveAd(
        final GameSession gameSession,
        final Message message
    ) {
        return messageService.solveAd(gameSession, message);
    }

    @Override
    public List<ShopItem> getAvailableItems(
        final GameSession gameSession
    ) {
        return shopService.getAvailableItems(gameSession);
    }

    @Override
    public PurchaseOutcome purchaseItem(
        final GameSession gameSession,
        final ShopItem shopItem
    ) {
        return shopService.purchaseItem(gameSession, shopItem);
    }
}
