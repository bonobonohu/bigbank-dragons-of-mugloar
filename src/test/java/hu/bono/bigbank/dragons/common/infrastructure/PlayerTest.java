package hu.bono.bigbank.dragons.common.infrastructure;

import hu.bono.bigbank.dragons.TestUtils;
import hu.bono.bigbank.dragons.common.domain.CharacterSheet;
import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.mission.domain.Message;
import hu.bono.bigbank.dragons.shop.domain.ShopItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static hu.bono.bigbank.dragons.common.infrastructure.Player.EXTRA_LIVES;
import static hu.bono.bigbank.dragons.common.infrastructure.Player.PURCHASE_LIVES_THRESHOLD;
import static org.mockito.ArgumentMatchers.any;

class PlayerTest {

    private final DungeonMaster dungeonMaster = Mockito.mock(DungeonMaster.class);
    private final Player underTest = new Player(dungeonMaster);

    @BeforeEach
    void beforeEach() {
        Mockito.reset(dungeonMaster);
        mockDungeonMasterPurchaseItem();
    }

    @Test
    void testPlayShouldStopRunningWhenCharacterIsDead() {
        final CharacterSheet characterSheet = TestUtils.createCharacterSheet();
        characterSheet.setLives(0);
        final GameSession gameSession = TestUtils.createGameSession(characterSheet);
        mockDungeonMasterStartGame(gameSession);
        underTest.play(characterSheet.getName(), 255);
        Mockito.verify(dungeonMaster, Mockito.times(0))
            .refreshMessages(any());
    }

    @Test
    void testPlayShouldStopRunningWhenMaxRunsReached() {
        final CharacterSheet characterSheet = TestUtils.createCharacterSheet();
        characterSheet.setLives(100);
        final GameSession gameSession = TestUtils.createGameSession(characterSheet);
        mockDungeonMasterStartGame(gameSession);
        underTest.play(characterSheet.getName(), 3);
        Mockito.verify(dungeonMaster, Mockito.times(3))
            .refreshMessages(any());
    }

    @Test
    void testDoHealShouldNotPurchaseLivesWhenNoNeedToHeal() {
        final CharacterSheet characterSheet = TestUtils.createCharacterSheet();
        characterSheet.setLives(PURCHASE_LIVES_THRESHOLD + 10);
        characterSheet.setGold(TestUtils.HEALING_POT_COST * 100);
        final GameSession gameSession = TestUtils.createGameSession(characterSheet);
        mockDungeonMasterStartGame(gameSession);
        underTest.play(characterSheet.getName(), 1);
        Mockito.verify(dungeonMaster, Mockito.times(0))
            .purchaseItem(any(), any());
    }

    @Test
    void testDoHealShouldNotPurchaseLivesWhenDoNotHaveGold() {
        final CharacterSheet characterSheet = TestUtils.createCharacterSheet();
        characterSheet.setLives(PURCHASE_LIVES_THRESHOLD - 1);
        characterSheet.setGold(1);
        final GameSession gameSession = TestUtils.createGameSession(characterSheet);
        gameSession.getShop().setItems(Set.of(TestUtils.HEALING_POT));
        mockDungeonMasterStartGame(gameSession);
        underTest.play(characterSheet.getName(), 1);
        Mockito.verify(dungeonMaster, Mockito.times(0))
            .purchaseItem(any(), any());
    }

    @Test
    void testDoHealShouldNotPurchaseExtraLivesWhenDoNotHaveGold() {
        final CharacterSheet characterSheet = TestUtils.createCharacterSheet();
        characterSheet.setLives(PURCHASE_LIVES_THRESHOLD - 1);
        characterSheet.setGold(TestUtils.HEALING_POT_COST + 1);
        final GameSession gameSession = TestUtils.createGameSession(characterSheet);
        gameSession.getShop().setItems(Set.of(TestUtils.HEALING_POT));
        mockDungeonMasterStartGame(gameSession);
        underTest.play(characterSheet.getName(), 1);
        Mockito.verify(dungeonMaster, Mockito.times(1))
            .purchaseItem(any(), any());
    }

    @Test
    void testDoHealShouldPurchaseExtraLivesWhenDoHaveGold() {
        final CharacterSheet characterSheet = TestUtils.createCharacterSheet();
        characterSheet.setLives(PURCHASE_LIVES_THRESHOLD - 1);
        characterSheet.setGold(TestUtils.HEALING_POT_COST * 2);
        final GameSession gameSession = TestUtils.createGameSession(characterSheet);
        gameSession.getShop().setItems(Set.of(TestUtils.HEALING_POT));
        mockDungeonMasterStartGame(gameSession);
        underTest.play(characterSheet.getName(), 1);
        Mockito.verify(dungeonMaster, Mockito.times(2))
            .purchaseItem(any(), any());
    }

    @Test
    void testDoHealShouldPurchaseExtraLivesOnlyWithinThreshold() {
        final CharacterSheet characterSheet = TestUtils.createCharacterSheet();
        characterSheet.setLives(PURCHASE_LIVES_THRESHOLD - 1);
        characterSheet.setGold(TestUtils.HEALING_POT_COST * 100);
        final GameSession gameSession = TestUtils.createGameSession(characterSheet);
        gameSession.getShop().setItems(Set.of(TestUtils.HEALING_POT));
        mockDungeonMasterStartGame(gameSession);
        underTest.play(characterSheet.getName(), 1);
        Mockito.verify(dungeonMaster, Mockito.times(1 + EXTRA_LIVES))
            .purchaseItem(any(), any());
    }

    @Test
    void testDoLevelUpShouldNotPurchaseItemWhenNoPurchasableItems() {
        final CharacterSheet characterSheet = TestUtils.createCharacterSheet();
        characterSheet.setGold(10_000_000);
        final GameSession gameSession = TestUtils.createGameSession(characterSheet);
        gameSession.getShop().setItems(TestUtils.SHOP_ITEMS);
        gameSession.setPurchasedItems(new HashSet<>(TestUtils.SHOP_ITEMS.values()));
        mockDungeonMasterStartGame(gameSession);
        underTest.play(characterSheet.getName(), 1);
        Mockito.verify(dungeonMaster, Mockito.times(0))
            .purchaseItem(any(), any());
    }

    @ParameterizedTest
    @MethodSource(
        "doLevelUpShouldPurchaseItemsAsLongAsHaveThresholdGold"
    )
    void testDoLevelUpShouldPurchaseItemsAsLongAsHaveThresholdGold(
        final int gold,
        final int noOfItemsPurchased
    ) {
        final CharacterSheet characterSheet = TestUtils.createCharacterSheet();
        characterSheet.setGold(gold);
        final GameSession gameSession = TestUtils.createGameSession(characterSheet);
        gameSession.getShop().setItems(TestUtils.SHOP_ITEMS);
        mockDungeonMasterStartGame(gameSession);
        underTest.play(characterSheet.getName(), 1);
        Mockito.verify(dungeonMaster, Mockito.times(noOfItemsPurchased))
            .purchaseItem(any(), any());
    }

    static Stream<Arguments> doLevelUpShouldPurchaseItemsAsLongAsHaveThresholdGold() {
        return Stream.of(
            Arguments.of(
                1,
                0
            ),
            Arguments.of(
                (TestUtils.HEALING_POT_COST * (EXTRA_LIVES + 1)) + 100,
                1
            ),
            Arguments.of(
                (TestUtils.HEALING_POT_COST * (EXTRA_LIVES + 1)) + 200,
                2
            ),
            Arguments.of(
                (TestUtils.HEALING_POT_COST * (EXTRA_LIVES + 1)) + 500,
                3
            ),
            Arguments.of(
                (TestUtils.HEALING_POT_COST * (EXTRA_LIVES + 1)) + 800,
                4
            ),
            Arguments.of(
                (TestUtils.HEALING_POT_COST * (EXTRA_LIVES + 1)) + 900,
                5
            ),
            Arguments.of(
                (TestUtils.HEALING_POT_COST * (EXTRA_LIVES + 1)) + 1000,
                6
            ),
            Arguments.of(
                (TestUtils.HEALING_POT_COST * (EXTRA_LIVES + 1)) + 1100,
                6
            ),
            Arguments.of(
                (TestUtils.HEALING_POT_COST * (EXTRA_LIVES + 1)) + 1200,
                6
            ),
            Arguments.of(
                (TestUtils.HEALING_POT_COST * (EXTRA_LIVES + 1)) + 1300,
                7
            ),
            Arguments.of(
                (TestUtils.HEALING_POT_COST * (EXTRA_LIVES + 1)) + 1400,
                7
            )
        );
    }

    @Test
    void testDoMissionShouldInvestigateReputationWhenNoMessages() {
        final CharacterSheet characterSheet = TestUtils.createCharacterSheet();
        characterSheet.setLevel(0);
        final GameSession gameSession = TestUtils.createGameSession(characterSheet);
        final Set<Message> messages = Collections.emptySet();
        mockDungeonMasterStartGame(gameSession);
        mockDungeonMasterRefreshMessages(messages);
        underTest.play(characterSheet.getName(), 1);
        Mockito.verify(dungeonMaster, Mockito.times(0))
            .goOnMission(any(), any());
        Mockito.verify(dungeonMaster, Mockito.times(1))
            .investigateReputation(any());
    }

    @ParameterizedTest
    @MethodSource(
        "doMissionShouldGoOnMission"
    )
    void testDoMissionShouldGoOnMission(
        final Integer level,
        final Set<Message> messages,
        final Message expected
    ) {
        final CharacterSheet characterSheet = TestUtils.createCharacterSheet();
        characterSheet.setLevel(level);
        final GameSession gameSession = TestUtils.createGameSession(characterSheet);
        mockDungeonMasterStartGame(gameSession);
        mockDungeonMasterRefreshMessages(messages);
        underTest.play(characterSheet.getName(), 1);
        Mockito.verify(dungeonMaster, Mockito.times(1))
            .goOnMission(gameSession, expected);
        Mockito.verify(dungeonMaster, Mockito.times(0))
            .investigateReputation(any());
    }

    // CHECKSTYLE:OFF
    static Stream<Arguments> doMissionShouldGoOnMission() {
        return Stream.of(
            // Single message within Probability threshold
            Arguments.of(
                0,
                Set.of(
                    TestUtils.createMessage(
                        "Ad1",
                        "Message 1",
                        100,
                        7,
                        Message.Probability.SURE_THING
                    )
                ),
                TestUtils.createMessage(
                    "Ad1",
                    "Message 1",
                    100,
                    7,
                    Message.Probability.SURE_THING
                )
            ),
            // Traps are ignored
            Arguments.of(
                0,
                Set.of(
                    TestUtils.createMessage(
                        "TrapAd",
                        "Steal super awesome diamond and some more bla-bla",
                        1_000_000,
                        1,
                        Message.Probability.SURE_THING
                    ),
                    TestUtils.createMessage(
                        "Ad2",
                        "Message 2",
                        100,
                        7,
                        Message.Probability.SURE_THING
                    )
                ),
                TestUtils.createMessage(
                    "Ad2",
                    "Message 2",
                    100,
                    7,
                    Message.Probability.SURE_THING
                )
            ),
            // Higher reward wins
            Arguments.of(
                0,
                Set.of(
                    TestUtils.createMessage(
                        "Ad1",
                        "Message 1",
                        100,
                        7,
                        Message.Probability.SURE_THING
                    ),
                    TestUtils.createMessage(
                        "Ad2",
                        "Message 2",
                        1_000_000,
                        7,
                        Message.Probability.SURE_THING
                    )
                ),
                TestUtils.createMessage(
                    "Ad2",
                    "Message 2",
                    1_000_000,
                    7,
                    Message.Probability.SURE_THING
                )
            ),
            // Lower probability wins
            Arguments.of(
                0,
                Set.of(
                    TestUtils.createMessage(
                        "Ad1",
                        "Message 1",
                        1_000_000,
                        7,
                        Message.Probability.PIECE_OF_CAKE
                    ),
                    TestUtils.createMessage(
                        "Ad2",
                        "Message 2",
                        1_000_000,
                        7,
                        Message.Probability.SURE_THING
                    )
                ),
                TestUtils.createMessage(
                    "Ad2",
                    "Message 2",
                    1_000_000,
                    7,
                    Message.Probability.SURE_THING
                )
            ),
            // Earlier expiration wins
            Arguments.of(
                0,
                Set.of(
                    TestUtils.createMessage(
                        "Ad1",
                        "Message 1",
                        1_000_000,
                        7,
                        Message.Probability.SURE_THING
                    ),
                    TestUtils.createMessage(
                        "Ad2",
                        "Message 2",
                        1_000_000,
                        1,
                        Message.Probability.SURE_THING
                    )
                ),
                TestUtils.createMessage(
                    "Ad2",
                    "Message 2",
                    1_000_000,
                    1,
                    Message.Probability.SURE_THING
                )
            ),
            // Level below or 8
            Arguments.of(
                6,
                Set.of(
                    TestUtils.createMessage(
                        "Ad1",
                        "Message 1",
                        1_000_000,
                        1,
                        Message.Probability.RATHER_DETRIMENTAL
                    ),
                    TestUtils.createMessage(
                        "Ad2",
                        "Message 2",
                        1,
                        7,
                        Message.Probability.QUITE_LIKELY
                    )
                ),
                TestUtils.createMessage(
                    "Ad2",
                    "Message 2",
                    1,
                    7,
                    Message.Probability.QUITE_LIKELY
                )
            ),
            // Level below or 16
            Arguments.of(
                16,
                Set.of(
                    TestUtils.createMessage(
                        "Ad1",
                        "Message 1",
                        1_000_000,
                        1,
                        Message.Probability.GAMBLE
                    ),
                    TestUtils.createMessage(
                        "Ad2",
                        "Message 2",
                        1,
                        7,
                        Message.Probability.RATHER_DETRIMENTAL
                    )
                ),
                TestUtils.createMessage(
                    "Ad2",
                    "Message 2",
                    1,
                    7,
                    Message.Probability.RATHER_DETRIMENTAL
                )
            ),
            // Level below or 32
            Arguments.of(
                30,
                Set.of(
                    TestUtils.createMessage(
                        "Ad1",
                        "Message 1",
                        1_000_000,
                        1,
                        Message.Probability.PLAYING_WITH_FIRE
                    ),
                    TestUtils.createMessage(
                        "Ad2",
                        "Message 2",
                        1,
                        7,
                        Message.Probability.GAMBLE
                    )
                ),
                TestUtils.createMessage(
                    "Ad2",
                    "Message 2",
                    1,
                    7,
                    Message.Probability.GAMBLE
                )
            ),
            // Level below or 48
            Arguments.of(
                40,
                Set.of(
                    TestUtils.createMessage(
                        "Ad1",
                        "Message 1",
                        1_000_000,
                        1,
                        Message.Probability.RISKY
                    ),
                    TestUtils.createMessage(
                        "Ad2",
                        "Message 2",
                        1,
                        7,
                        Message.Probability.PLAYING_WITH_FIRE
                    )
                ),
                TestUtils.createMessage(
                    "Ad2",
                    "Message 2",
                    1,
                    7,
                    Message.Probability.PLAYING_WITH_FIRE
                )
            ),
            // Level below or 64
            Arguments.of(
                60,
                Set.of(
                    TestUtils.createMessage(
                        "Ad1",
                        "Message 1",
                        1_000_000,
                        1,
                        Message.Probability.SUICIDE_MISSION
                    ),
                    TestUtils.createMessage(
                        "Ad2",
                        "Message 2",
                        1,
                        7,
                        Message.Probability.RISKY
                    )
                ),
                TestUtils.createMessage(
                    "Ad2",
                    "Message 2",
                    1,
                    7,
                    Message.Probability.RISKY
                )
            ),
            // Level below or 128
            Arguments.of(
                100,
                Set.of(
                    TestUtils.createMessage(
                        "Ad1",
                        "Message 1",
                        1_000_000,
                        1,
                        Message.Probability.IMPOSSIBLE
                    ),
                    TestUtils.createMessage(
                        "Ad2",
                        "Message 2",
                        1,
                        7,
                        Message.Probability.SUICIDE_MISSION
                    )
                ),
                TestUtils.createMessage(
                    "Ad2",
                    "Message 2",
                    1,
                    7,
                    Message.Probability.SUICIDE_MISSION
                )
            ),
            // Level below or 256
            Arguments.of(
                200,
                Set.of(
                    TestUtils.createMessage(
                        "Ad1",
                        "Message 1",
                        1_000_000,
                        1,
                        Message.Probability.HMMM
                    ),
                    TestUtils.createMessage(
                        "Ad2",
                        "Message 2",
                        1,
                        7,
                        Message.Probability.IMPOSSIBLE
                    )
                ),
                TestUtils.createMessage(
                    "Ad2",
                    "Message 2",
                    1,
                    7,
                    Message.Probability.IMPOSSIBLE
                )
            ),
            // Level above 256 unlocks all Probabilities
            Arguments.of(
                257,
                Set.of(
                    TestUtils.createMessage(
                        "Ad1",
                        "Message 1",
                        1,
                        7,
                        Message.Probability.HMMM
                    ),
                    TestUtils.createMessage(
                        "Ad2",
                        "Message 2",
                        1_000_000,
                        1,
                        Message.Probability.HMMM
                    )
                ),
                TestUtils.createMessage(
                    "Ad2",
                    "Message 2",
                    1_000_000,
                    1,
                    Message.Probability.HMMM
                )
            )
        );
    }
    // CHECKSTYLE:ON

    private void mockDungeonMasterStartGame(
        final GameSession gameSession
    ) {
        Mockito.doAnswer(invocation -> gameSession)
            .when(dungeonMaster).startGame(any());
    }

    private void mockDungeonMasterPurchaseItem() {
        Mockito.doAnswer(invocation -> {
                final Object[] args = invocation.getArguments();
                final GameSession gameSession = (GameSession) args[0];
                final ShopItem shopItem = (ShopItem) args[1];
                gameSession.getCharacterSheet()
                    .setGold(
                        gameSession.getCharacterSheet().getGold()
                            - shopItem.cost()
                    );
                gameSession.getCharacterSheet()
                    .setLives(
                        gameSession.getCharacterSheet().getLives()
                            + shopItem.getLivesGain()
                    );
                gameSession.getCharacterSheet()
                    .setLevel(
                        gameSession.getCharacterSheet().getLevel()
                            + shopItem.getLevelGain()
                    );
                updatePurchasedItems(gameSession, shopItem);
                return null;
            })
            .when(dungeonMaster).purchaseItem(any(), any());
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

    private void mockDungeonMasterRefreshMessages(
        final Set<Message> messages
    ) {
        Mockito.doAnswer(invocation -> {
                final Object[] args = invocation.getArguments();
                final GameSession gameSession = (GameSession) args[0];
                gameSession.setMessages(messages);
                return null;
            })
            .when(dungeonMaster).refreshMessages(any());
    }
}
