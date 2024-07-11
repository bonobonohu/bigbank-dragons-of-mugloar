package hu.bono.bigbank.dragons.investigation.application;

import lombok.Builder;

@Builder
public record PostInvestigateReputationResponse(
    Double people,
    Integer state,
    Integer underworld
) {

}
