package hu.bono.bigbank.dragons.common.infrastructure;

import org.springframework.stereotype.Component;

@Component
public class PlayerFactory {

    public Player createPlayer(
        final DungeonMaster dungeonMaster
    ) {
        return new Player(dungeonMaster);
    }
}
