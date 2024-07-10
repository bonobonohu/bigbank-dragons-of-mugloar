package hu.bono.bigbank.dragons.game.infrastructure;

import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.game.application.PostGameStartResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

@Mapper(imports = Instant.class)
public interface PostGameStartResponseMapper {

    PostGameStartResponseMapper MAPPER = Mappers.getMapper(PostGameStartResponseMapper.class);

    @Mapping(target = "creationTimestamp", expression = "java(Instant.now())")
    GameSession postGameStartResponseToGameSession(PostGameStartResponse postGameStartResponse);
}
