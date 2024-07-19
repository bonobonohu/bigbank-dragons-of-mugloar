package hu.bono.bigbank.dragons.common.infrastructure;

import hu.bono.bigbank.dragons.common.domain.CharacterSheet;
import hu.bono.bigbank.dragons.common.domain.GameMapper;
import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.common.domain.RestClientClientException;
import hu.bono.bigbank.dragons.game.domain.Game;
import hu.bono.bigbank.dragons.investigation.domain.Reputation;
import hu.bono.bigbank.dragons.mission.domain.Message;
import hu.bono.bigbank.dragons.mission.domain.MissionOutcome;
import hu.bono.bigbank.dragons.shop.domain.PurchaseOutcome;
import hu.bono.bigbank.dragons.shop.domain.ShopItem;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DungeonMaster {

    private static final int MAX_API_ATTEMPTS = 5;

    private static final Logger LOG = LoggerFactory.getLogger(DungeonMaster.class);

    private final Api api;
    private final GameMapper gameMapper;
    private final LogWriter logWriter;

    public GameSession startGame(
        final CharacterSheet characterSheet
    ) {
        final Game game = api.startGame();
        final GameSession gameSession = gameMapper.gameToGameSession(game, characterSheet);
        logWriter.log(gameSession, "startGame", null, gameSession);
        return gameSession;
    }

    public void loadShop(
        final GameSession gameSession
    ) {
        final List<ShopItem> shopItems = api.getShopItems(gameSession.getGameId());
        gameSession.getShop().setItems(shopItems);
        logWriter.log(gameSession, "loadShop", null, shopItems);
    }

    public void purchaseItem(
        final GameSession gameSession,
        final ShopItem shopItem
    ) {
        if (notEnoughMoney(gameSession, shopItem.cost())) {
            LOG.error("Not enough money for purchasing");
            return;
        }
        PurchaseOutcome purchaseOutcome;
        int attempts = 0;
        do {
            purchaseOutcome = api.purchaseItem(gameSession.getGameId(), shopItem.id());
            logWriter.log(gameSession, "purchaseItemAttempt", shopItem, purchaseOutcome);
            setTurn(gameSession, purchaseOutcome.turn());
            attempts++;
        } while (
            !purchaseSuccess(purchaseOutcome)
                && attempts < MAX_API_ATTEMPTS
        );
        if (purchaseSuccess(purchaseOutcome)) {
            updateCharacterSheetAfterPurchase(gameSession, shopItem, purchaseOutcome);
            logWriter.log(gameSession, "purchaseItemSuccess", shopItem, purchaseOutcome);
        } else {
            logWriter.log(gameSession, "purchaseItemFailure", shopItem, purchaseOutcome);
        }
    }

    private boolean notEnoughMoney(
        final GameSession gameSession,
        final int cost
    ) {
        return gameSession.getCharacterSheet().getGold() < cost;
    }

    private boolean purchaseSuccess(
        final PurchaseOutcome purchaseOutcome
    ) {
        return purchaseOutcome.shoppingSuccess();
    }

    private void updateCharacterSheetAfterPurchase(
        final GameSession gameSession,
        final ShopItem shopItem,
        final PurchaseOutcome purchaseOutcome
    ) {
        updateBasicBookAfterPurchase(gameSession, purchaseOutcome);
        updateMyBookAfterPurchase(gameSession, shopItem);
        updatePurchasedItems(gameSession, shopItem);
    }

    private void updateBasicBookAfterPurchase(
        final GameSession gameSession,
        final PurchaseOutcome purchaseOutcome
    ) {
        gameSession.getCharacterSheet().setGold(purchaseOutcome.gold());
        gameSession.getCharacterSheet().setLives(purchaseOutcome.lives());
        gameSession.getCharacterSheet().setLevel(purchaseOutcome.level());
    }

    private void updateMyBookAfterPurchase(
        final GameSession gameSession,
        final ShopItem shopItem
    ) {
        gameSession.getCharacterSheet().getMyBook().setGold(
            gameSession.getCharacterSheet().getMyBook().getGold()
                - shopItem.cost()
        );
        gameSession.getCharacterSheet().getMyBook().setLives(
            gameSession.getCharacterSheet().getMyBook().getLives()
                + shopItem.getLivesGain()
        );
        gameSession.getCharacterSheet().getMyBook().setLevel(
            gameSession.getCharacterSheet().getMyBook().getLevel()
                + shopItem.getLevelGain()
        );
    }

    private void updatePurchasedItems(
        final GameSession gameSession,
        final ShopItem shopItem
    ) {
        if (!shopItem.isHealingPot()) {
            gameSession.getPurchasedItems().add(shopItem);
            if (haveAllLevelUpItems(gameSession)) {
                gameSession.getPurchasedItems().clear();
            }
        }
    }

    private boolean haveAllLevelUpItems(
        final GameSession gameSession
    ) {
        return gameSession.getPurchasedItems().size()
            == gameSession.getShop().getLevelUpItemsCount();
    }

    public void refreshMessages(
        final GameSession gameSession
    ) {
        final List<Message> fakeMessages = api.getMessages(gameSession.getGameId());
        final List<Message> messages = api.getMessages(gameSession.getGameId());
        gameSession.setMessages(new HashSet<>(messages));
        logWriter.log(gameSession, "fakeMessages", null, fakeMessages);
        logWriter.log(gameSession, "refreshMessages", null, messages);
    }

    public void goOnMission(
        final GameSession gameSession,
        final Message message
    ) {
        MissionOutcome missionOutcome = null;
        int attempts = 0;
        do {
            try {
                missionOutcome = api.goOnMission(gameSession.getGameId(), message.adId());
                logWriter.log(gameSession, "goOnMissionAttempt", message, missionOutcome);
            } catch (RestClientClientException exception) {
                // Sadly, this is kinda expected here, in order to tackle the effects of flakiness
            }
            attempts++;
        } while (attempts < MAX_API_ATTEMPTS);
        if (missionOutcome != null) {
            updateCharacterSheetAfterMission(gameSession, message, missionOutcome);
            if (missionSuccess(missionOutcome)) {
                logWriter.log(gameSession, "goOnMissionSuccess", message, missionOutcome);
            } else if (missionFailureSurvived(missionOutcome)) {
                logWriter.log(gameSession, "goOnMissionFailure", message, missionOutcome);
            } else {
                logWriter.log(gameSession, "goOnMissionDie", message, missionOutcome);
                logWriter.log(gameSession, "dieDieDie", missionOutcome, gameSession.getCharacterSheet());
            }
            setTurn(gameSession, missionOutcome.turn());
        }
    }

    private boolean missionSuccess(
        final MissionOutcome missionOutcome
    ) {
        return missionOutcome != null
            && missionOutcome.success();
    }

    private boolean missionFailureSurvived(
        final MissionOutcome missionOutcome
    ) {
        return missionOutcome != null
            && !missionOutcome.success()
            && missionOutcome.lives() > 0;
    }

    private void updateCharacterSheetAfterMission(
        final GameSession gameSession,
        final Message message,
        final MissionOutcome missionOutcome
    ) {
        updateBasicBookAfterMission(gameSession, missionOutcome);
        updateMyBookAfterMission(gameSession, message, missionOutcome);
    }

    private void updateBasicBookAfterMission(
        final GameSession gameSession,
        final MissionOutcome missionOutcome
    ) {
        gameSession.getCharacterSheet().setLives(missionOutcome.lives());
        gameSession.getCharacterSheet().setGold(missionOutcome.gold());
        gameSession.getCharacterSheet().setScore(missionOutcome.score());
        gameSession.getCharacterSheet().setHighScore(missionOutcome.highScore());
    }

    private void updateMyBookAfterMission(
        final GameSession gameSession,
        final Message message,
        final MissionOutcome missionOutcome
    ) {
        gameSession.getCharacterSheet().getMyBook().setLives(
            gameSession.getCharacterSheet().getMyBook().getLives()
                + (missionOutcome.success() ? 0 : -1)
        );
        gameSession.getCharacterSheet().getMyBook().setGold(
            gameSession.getCharacterSheet().getMyBook().getGold()
                + (missionOutcome.success() ? message.reward() : 0)
        );
        gameSession.getCharacterSheet().getMyBook().setScore(
            gameSession.getCharacterSheet().getMyBook().getScore()
                + (missionOutcome.success() ? message.reward() : 0)
        );
    }

    public void investigateReputation(
        final GameSession gameSession
    ) {
        final Reputation fakeReputation = api.investigateReputation(gameSession.getGameId());
        final Reputation reputation = api.investigateReputation(gameSession.getGameId());
        gameSession.getCharacterSheet().setReputation(reputation);
        logWriter.log(gameSession, "fakeReputation", null, fakeReputation);
        logWriter.log(gameSession, "investigateReputation", null, reputation);
        setTurn(gameSession, gameSession.getTurn() + 1);
    }

    private void setTurn(
        final GameSession gameSession,
        final int newTurn
    ) {
        gameSession.setTurn(newTurn);
        gameSession.getMyBook().setTurn(
            gameSession.getMyBook().getTurn() + 1
        );
    }
}
