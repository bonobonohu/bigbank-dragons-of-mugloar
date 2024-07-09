package hu.bono.bigbank.dragons.game.infrastructure;

import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.game.application.PostGameResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

@Mapper(imports = Instant.class)
public interface PostGameResponseMapper {

    PostGameResponseMapper MAPPER = Mappers.getMapper(PostGameResponseMapper.class);

    @Mapping(target = "creationTimestamp", expression = "java(Instant.now())")
    GameSession postGameResponseToGameSession(PostGameResponse postGameResponse);
}
