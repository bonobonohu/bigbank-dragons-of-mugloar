package hu.bono.bigbank.dragons.game.application;

import hu.bono.bigbank.dragons.common.domain.GameSession;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

@Mapper(
    imports = Instant.class
)
public interface PostGameStartResponseMapper {

    PostGameStartResponseMapper MAPPER = Mappers.getMapper(PostGameStartResponseMapper.class);

    @Mapping(
        target = "creationTimestamp",
        expression = "java(Instant.now())"
    )
    @Mapping(
        target = "reputation",
        ignore = true
    )
    @Mapping(
        target = "shopCache",
        ignore = true
    )
    @Mapping(
        target = "purchasedItems",
        ignore = true
    )
    @Mapping(
        target = "messages",
        ignore = true
    )
    GameSession postGameStartResponseToGameSession(
        PostGameStartResponse postGameStartResponse
    );
}
