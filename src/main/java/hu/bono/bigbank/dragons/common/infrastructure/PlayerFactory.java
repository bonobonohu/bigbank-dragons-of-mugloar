package hu.bono.bigbank.dragons.common.infrastructure;

import hu.bono.bigbank.dragons.common.application.PlayerConfiguration;
import org.springframework.stereotype.Component;

@Component
public class PlayerFactory {

    public Player createPlayer(
        final DungeonMaster dungeonMaster,
        final PlayerConfiguration playerConfiguration
    ) {
        return new Player(dungeonMaster, playerConfiguration);
    }
}
