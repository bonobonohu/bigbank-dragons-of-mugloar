package hu.bono.bigbank.dragons.game.application;

import hu.bono.bigbank.dragons.TestUtils;
import hu.bono.bigbank.dragons.game.domain.Game;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PostGameStartResponseMapperTest {

    private final PostGameStartResponseMapper underTest = PostGameStartResponseMapper.MAPPER;

    @Test
    void testPostGameStartResponseToGameWhenPostGameStartResponseIsNull() {
        final Game actual = underTest.postGameStartResponseToGame(null);
        Assertions.assertThat(actual).isNull();
    }

    @Test
    void testPostGameStartResponseToGameWhenPostGameStartResponseIsNotNull() {
        final Game expected = TestUtils.createGame();
        final Game actual = underTest.postGameStartResponseToGame(
            TestUtils.createPostGameStartResponse()
        );
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}
