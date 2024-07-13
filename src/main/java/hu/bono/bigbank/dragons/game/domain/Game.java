package hu.bono.bigbank.dragons.game.domain;

import lombok.Builder;

@Builder
public record Game(
    String gameId,
    Integer lives,
    Integer gold,
    Integer level,
    Integer score,
    Integer highScore,
    Integer turn
) {

}
