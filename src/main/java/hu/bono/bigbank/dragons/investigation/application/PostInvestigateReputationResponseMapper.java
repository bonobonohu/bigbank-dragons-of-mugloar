package hu.bono.bigbank.dragons.investigation.application;

import hu.bono.bigbank.dragons.investigation.domain.Reputation;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

@Mapper(imports = Instant.class)
public interface PostInvestigateReputationResponseMapper {

    PostInvestigateReputationResponseMapper MAPPER = Mappers.getMapper(PostInvestigateReputationResponseMapper.class);

    Reputation postInvestigateReputationResponseToReputation(
        PostInvestigateReputationResponse postInvestigateReputationResponse
    );
}
