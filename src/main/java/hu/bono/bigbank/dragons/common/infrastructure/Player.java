package hu.bono.bigbank.dragons.common.infrastructure;

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

    private static final int MAX_RUNS = 255;
    private static final int BUY_LIVES_THRESHOLD = 2;
    private static final int EXTRA_LIVES = 1;

    private static final Logger LOG = LoggerFactory.getLogger(Player.class);

    private final DungeonMaster dungeonMaster;

    public void play(
        final String characterName
    ) {
        final CharacterSheet characterSheet = CharacterSheet.builder()
            .name(characterName)
            .build();
        final GameSession gameSession = dungeonMaster.startGame(characterSheet);
        dungeonMaster.loadShop(gameSession);
        int run = 0;
        do {
            run++;
            doActions(gameSession);
            LOG.info(
                "{}@{}, do-while: {}/{}, turn: {}",
                gameSession.getCharacterSheet().getName(), gameSession.getGameId(), run, MAX_RUNS, gameSession.getTurn()
            );
        } while (
            gameSession.getCharacterSheet().getLives() > 0
                && run < MAX_RUNS
        );
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
        if (needToHeal(gameSession) && haveMoney(gameSession, ShopItem.HEALING_POT_COST)) {
            dungeonMaster.purchaseItem(gameSession, gameSession.getShop().get(ShopItem.HEALING_POT_ID));
            int extraPurchased = 0;
            while (
                haveMoney(gameSession, ShopItem.HEALING_POT_COST)
                    && extraPurchased < EXTRA_LIVES
            ) {
                dungeonMaster.purchaseItem(gameSession, gameSession.getShop().get(ShopItem.HEALING_POT_ID));
                extraPurchased++;
            }
        }
    }

    private boolean needToHeal(
        final GameSession gameSession
    ) {
        return gameSession.getCharacterSheet().getLives() <= BUY_LIVES_THRESHOLD;
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
        ShopItem purchasedShopItem = null;
        while (
            haveMoney(gameSession, levelUpMoneyThreshold(gameSession, shopItemToPurchase))
                && shopItemToPurchase != null
                && !shopItemToPurchase.equals(purchasedShopItem)
        ) {
            dungeonMaster.purchaseItem(gameSession, shopItemToPurchase);
            purchasedShopItem = shopItemToPurchase;
            shopItemToPurchase = getShopItemToPurchase(gameSession);
        }
    }

    private int levelUpMoneyThreshold(
        final GameSession gameSession,
        final ShopItem shopItem
    ) {
        return (gameSession.getShop().get(ShopItem.HEALING_POT_ID).cost() * (EXTRA_LIVES + 1))
            + shopItem.cost();
    }

    private ShopItem getShopItemToPurchase(
        final GameSession gameSession
    ) {
        final Set<ShopItem> shopItems = new HashSet<>(gameSession.getShop().values());
        shopItems.removeAll(gameSession.getCharacterSheet().getPurchasedItems());
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
        } else if (level <= 10) {
            thresholds.add(Message.Probability.QUITE_LIKELY);
            thresholds.add(Message.Probability.RATHER_DETRIMENTAL);
        } else if (level <= 12) {
            thresholds.add(Message.Probability.QUITE_LIKELY);
            thresholds.add(Message.Probability.RATHER_DETRIMENTAL);
            thresholds.add(Message.Probability.GAMBLE);
        } else {
            thresholds.add(Message.Probability.QUITE_LIKELY);
            thresholds.add(Message.Probability.RATHER_DETRIMENTAL);
            thresholds.add(Message.Probability.GAMBLE);
            thresholds.add(Message.Probability.PLAYING_WITH_FIRE);
        }
        return thresholds;
    }

    private Optional<Message> selectMessageBasedOnThreshold(
        final GameSession gameSession,
        final Message.Probability threshold
    ) {
        return gameSession.getMessages().stream()
            .filter(message -> !message.itsATrap())
            .filter(message -> message.probability().getValue() <= threshold.getValue())
            .min(
                Comparator.comparingInt(Message::reward).reversed()
                    .thenComparingInt(message -> message.probability().getValue())
                    .thenComparingInt(Message::expiresIn)
            );
    }
}
