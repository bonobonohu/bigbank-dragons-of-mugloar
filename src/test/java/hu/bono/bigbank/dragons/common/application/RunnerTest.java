package hu.bono.bigbank.dragons.common.application;

import hu.bono.bigbank.dragons.common.infrastructure.DungeonMaster;
import hu.bono.bigbank.dragons.common.infrastructure.Player;
import hu.bono.bigbank.dragons.common.infrastructure.PlayerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.*;

class RunnerTest {

    private final DungeonMaster dungeonMaster = Mockito.mock(DungeonMaster.class);
    private final PlayerFactory playerFactory = Mockito.mock(PlayerFactory.class);
    private final Runner underTest = new Runner(dungeonMaster, playerFactory);

    @BeforeEach
    void beforeEach() {
        Mockito.reset(dungeonMaster, playerFactory);
    }

    @Test
    void testRun() {
        final Player player = Mockito.mock(Player.class);
        Mockito.when(playerFactory.createPlayer(any()))
            .thenReturn(player);
        underTest.run();
        Mockito.verify(playerFactory, Mockito.atLeastOnce())
            .createPlayer(any());
        Mockito.verify(player, Mockito.atLeastOnce())
            .play(anyString(), anyInt());
    }
}
