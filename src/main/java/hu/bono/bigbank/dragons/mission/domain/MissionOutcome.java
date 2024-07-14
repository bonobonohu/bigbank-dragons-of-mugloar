package hu.bono.bigbank.dragons.mission.domain;

import lombok.Builder;

@Builder
public record MissionOutcome(
    Boolean success,
    Integer lives,
    Integer gold,
    Integer score,
    Integer highScore,
    Integer turn,
    String message
) {

}
