package hu.bono.bigbank.dragons.common.infrastructure;

import hu.bono.bigbank.dragons.common.application.PlayerConfiguration;
import hu.bono.bigbank.dragons.common.domain.CharacterSheet;
import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.mission.domain.Message;
import hu.bono.bigbank.dragons.shop.domain.ShopItem;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@RequiredArgsConstructor
public class Player {

    private static final Logger LOG = LoggerFactory.getLogger(Player.class);

    private final DungeonMaster dungeonMaster;
    private final PlayerConfiguration playerConfiguration;

    public void play(
        final String characterName,
        final int maxRuns
    ) {
        final CharacterSheet characterSheet = CharacterSheet.builder()
            .name(characterName)
            .build();
        final GameSession gameSession = dungeonMaster.startGame(characterSheet);
        dungeonMaster.loadShop(gameSession);
        int run = 0;
        while (
            gameSession.getCharacterSheet().getLives() > 0
                && run < maxRuns
        ) {
            doActions(gameSession);
            LOG.info(
                "{}@{}, do-while: {}/{}, turn: {}",
                gameSession.getCharacterSheet().getName(), gameSession.getGameId(), run, maxRuns, gameSession.getTurn()
            );
            run++;
        }
        LOG.info("Game over: {}", gameSession);
    }

    private void doActions(
        final GameSession gameSession
    ) {
        doShopping(gameSession);
        doMission(gameSession);
    }

    private void doShopping(
        final GameSession gameSession
    ) {
        doHeal(gameSession);
        doLevelUp(gameSession);
    }

    private void doHeal(
        final GameSession gameSession
    ) {
        if (needToHeal(gameSession) && haveMoney(gameSession, healingPotCost(gameSession))) {
            final ShopItem healingPot = gameSession.getShop().getHPot();
            dungeonMaster.purchaseItem(gameSession, healingPot);
            int extraPurchased = 0;
            while (
                haveMoney(gameSession, healingPotCost(gameSession))
                    && extraPurchased < playerConfiguration.getExtraLives()
            ) {
                dungeonMaster.purchaseItem(gameSession, healingPot);
                extraPurchased++;
            }
        }
    }

    private boolean needToHeal(
        final GameSession gameSession
    ) {
        return gameSession.getCharacterSheet().getLives() <= playerConfiguration.getPurchaseLivesThreshold();
    }

    private int healingPotCost(
        final GameSession gameSession
    ) {
        return gameSession.getShop().getHPot().cost();
    }

    private boolean haveMoney(
        final GameSession gameSession,
        final int cost
    ) {
        return gameSession.getCharacterSheet().getGold() >= cost;
    }

    private void doLevelUp(
        final GameSession gameSession
    ) {
        ShopItem shopItemToPurchase = getShopItemToPurchase(gameSession);
        ShopItem attemptedShopItem = null;
        while (
            shopItemToPurchase != null
                && haveMoney(gameSession, levelUpMoneyThreshold(gameSession, shopItemToPurchase))
                && !shopItemToPurchase.equals(attemptedShopItem)
        ) {
            dungeonMaster.purchaseItem(gameSession, shopItemToPurchase);
            attemptedShopItem = shopItemToPurchase;
            shopItemToPurchase = getShopItemToPurchase(gameSession);
        }
    }

    private ShopItem getShopItemToPurchase(
        final GameSession gameSession
    ) {
        final Set<ShopItem> shopItems = gameSession.getShop().getItemsAsSet();
        shopItems.removeAll(gameSession.getPurchasedItems());
        final Optional<ShopItem> shopItemToPurchase = shopItems.stream()
            .filter(shopItem -> shopItem.cost() == ShopItem.LEVEL_ONE_ITEM_COST)
            .findFirst()
            .or(() -> shopItems.stream()
                .filter(shopItem -> shopItem.cost() == ShopItem.LEVEL_TWO_ITEM_COST)
                .findFirst()
            );
        if (shopItemToPurchase.isEmpty()) {
            LOG.error("Can't find ShopItem to purchase");
        }
        return shopItemToPurchase.orElse(null);
    }

    private int levelUpMoneyThreshold(
        final GameSession gameSession,
        final ShopItem shopItem
    ) {
        return (healingPotCost(gameSession) * (playerConfiguration.getExtraLives() + 1)) + shopItem.cost();
    }

    private void doMission(
        final GameSession gameSession
    ) {
        dungeonMaster.refreshMessages(gameSession);
        final List<Message.Probability> thresholds = getThresholdsAccordingToLevel(gameSession);
        Optional<Message> selectedMessage = Optional.empty();
        for (Message.Probability threshold : thresholds) {
            selectedMessage = selectMessageBasedOnThreshold(gameSession, threshold);
            if (selectedMessage.isPresent()) {
                break;
            }
        }
        selectedMessage.ifPresentOrElse(
            message -> dungeonMaster.goOnMission(gameSession, message),
            () -> dungeonMaster.investigateReputation(gameSession)
        );
    }

    private List<Message.Probability> getThresholdsAccordingToLevel(
        final GameSession gameSession
    ) {
        final List<Message.Probability> thresholds = new ArrayList<>();
        final int level = gameSession.getCharacterSheet().getLevel();
        if (level <= 8) {
            thresholds.add(Message.Probability.QUITE_LIKELY);
        } else if (level <= 16) {
            thresholds.add(Message.Probability.QUITE_LIKELY);
            thresholds.add(Message.Probability.RATHER_DETRIMENTAL);
        } else if (level <= 32) {
            thresholds.add(Message.Probability.QUITE_LIKELY);
            thresholds.add(Message.Probability.RATHER_DETRIMENTAL);
            thresholds.add(Message.Probability.GAMBLE);
        } else if (level <= 48) {
            thresholds.add(Message.Probability.QUITE_LIKELY);
            thresholds.add(Message.Probability.RATHER_DETRIMENTAL);
            thresholds.add(Message.Probability.GAMBLE);
            thresholds.add(Message.Probability.PLAYING_WITH_FIRE);
        } else if (level <= 64) {
            thresholds.add(Message.Probability.QUITE_LIKELY);
            thresholds.add(Message.Probability.RATHER_DETRIMENTAL);
            thresholds.add(Message.Probability.GAMBLE);
            thresholds.add(Message.Probability.PLAYING_WITH_FIRE);
            thresholds.add(Message.Probability.RISKY);
        } else if (level <= 128) {
            thresholds.add(Message.Probability.QUITE_LIKELY);
            thresholds.add(Message.Probability.RATHER_DETRIMENTAL);
            thresholds.add(Message.Probability.GAMBLE);
            thresholds.add(Message.Probability.PLAYING_WITH_FIRE);
            thresholds.add(Message.Probability.RISKY);
            thresholds.add(Message.Probability.SUICIDE_MISSION);
        } else if (level <= 256) {
            thresholds.add(Message.Probability.QUITE_LIKELY);
            thresholds.add(Message.Probability.RATHER_DETRIMENTAL);
            thresholds.add(Message.Probability.GAMBLE);
            thresholds.add(Message.Probability.PLAYING_WITH_FIRE);
            thresholds.add(Message.Probability.RISKY);
            thresholds.add(Message.Probability.SUICIDE_MISSION);
            thresholds.add(Message.Probability.IMPOSSIBLE);
        } else {
            thresholds.add(Message.Probability.QUITE_LIKELY);
            thresholds.add(Message.Probability.RATHER_DETRIMENTAL);
            thresholds.add(Message.Probability.GAMBLE);
            thresholds.add(Message.Probability.PLAYING_WITH_FIRE);
            thresholds.add(Message.Probability.RISKY);
            thresholds.add(Message.Probability.SUICIDE_MISSION);
            thresholds.add(Message.Probability.IMPOSSIBLE);
            thresholds.add(Message.Probability.HMMM);
        }
        return thresholds;
    }

    private Optional<Message> selectMessageBasedOnThreshold(
        final GameSession gameSession,
        final Message.Probability threshold
    ) {
        return gameSession.getMessages().stream()
            .filter(message -> message.probability().getValue() <= threshold.getValue())
            .filter(message -> !message.itsATrap())
            .min(
                Comparator.comparingInt(Message::reward).reversed()
                    .thenComparingInt(message -> message.probability().getValue())
                    .thenComparingInt(Message::expiresIn)
            );
    }
}
