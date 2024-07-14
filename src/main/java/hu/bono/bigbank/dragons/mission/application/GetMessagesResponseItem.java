package hu.bono.bigbank.dragons.mission.application;

import lombok.Builder;

@Builder
public record GetMessagesResponseItem(
    String adId,
    String message,
    Integer reward,
    Integer expiresIn,
    Integer encrypted,
    String probability
) {

}
