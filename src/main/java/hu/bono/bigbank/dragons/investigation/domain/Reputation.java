package hu.bono.bigbank.dragons.investigation.domain;

import lombok.Builder;

@Builder
public record Reputation(
    Double people,
    Integer state,
    Integer underworld
) {

}
