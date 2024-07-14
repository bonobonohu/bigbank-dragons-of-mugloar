package hu.bono.bigbank.dragons.mission.application;

import hu.bono.bigbank.dragons.mission.domain.MissionOutcome;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostSolveAdResponseMapper {

    PostSolveAdResponseMapper MAPPER = Mappers.getMapper(PostSolveAdResponseMapper.class);

    MissionOutcome postSolveAdResponseToMissionOutcome(
        PostSolveAdResponse postSolveAdResponse
    );
}
