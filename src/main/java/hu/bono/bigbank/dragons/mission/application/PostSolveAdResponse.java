package hu.bono.bigbank.dragons.mission.application;

import lombok.Builder;

@Builder
public record PostSolveAdResponse(
    Boolean success,
    Integer lives,
    Integer gold,
    Integer score,
    Integer highScore,
    Integer turn,
    String message
) {

}
