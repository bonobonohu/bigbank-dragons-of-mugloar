package hu.bono.bigbank.dragons.investigate.application;

import lombok.Builder;

@Builder
public record PostInvestigateReputationResponse(
    Double people,
    Integer state,
    Integer underworld
) {

}
