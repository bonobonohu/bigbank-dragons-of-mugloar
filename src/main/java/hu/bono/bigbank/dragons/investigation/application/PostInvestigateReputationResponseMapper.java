package hu.bono.bigbank.dragons.investigation.application;

import hu.bono.bigbank.dragons.investigation.domain.Reputation;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostInvestigateReputationResponseMapper {

    PostInvestigateReputationResponseMapper MAPPER = Mappers.getMapper(PostInvestigateReputationResponseMapper.class);

    Reputation postInvestigateReputationResponseToReputation(
        PostInvestigateReputationResponse postInvestigateReputationResponse
    );
}
