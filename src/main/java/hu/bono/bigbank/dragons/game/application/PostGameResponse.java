package hu.bono.bigbank.dragons.game.application;

import lombok.Generated;

@Generated
public record PostGameResponse(
    String gameId,
    Integer lives,
    Integer gold,
    Integer level,
    Integer score,
    Integer highScore,
    Integer turn
) {

}
