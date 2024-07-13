package hu.bono.bigbank.dragons.game.application;

import hu.bono.bigbank.dragons.game.domain.Game;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostGameStartResponseMapper {

    PostGameStartResponseMapper MAPPER = Mappers.getMapper(PostGameStartResponseMapper.class);

    Game postGameStartResponseToGame(
        PostGameStartResponse postGameStartResponse
    );
}
