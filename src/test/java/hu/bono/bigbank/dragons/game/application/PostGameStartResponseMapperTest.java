package hu.bono.bigbank.dragons.game.application;

import hu.bono.bigbank.dragons.TestUtils;
import hu.bono.bigbank.dragons.common.domain.GameSession;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.Instant;

import static org.mockito.Mockito.mockStatic;

class PostGameStartResponseMapperTest {

    private final PostGameStartResponseMapper underTest = PostGameStartResponseMapper.MAPPER;

    @Test
    void testPostGameStartResponseToGameSessionWhenPostGameStartResponseIsNull() {
        final GameSession actual = underTest.postGameStartResponseToGameSession(null);
        Assertions.assertThat(actual).isNull();
    }

    @Test
    void testPostGameStartResponseToGameSessionWhenPostGameStartResponseIsNotNull() {
        try (MockedStatic<Instant> mockedInstant = mockStatic(Instant.class)) {
            final Instant fixedInstant = Instant.parse("2024-07-09T20:27:42Z");
            mockedInstant.when(Instant::now).thenReturn(fixedInstant);

            final GameSession expected = TestUtils.createGameSession(fixedInstant);
            final GameSession actual = underTest.postGameStartResponseToGameSession(
                TestUtils.createPostGameStartResponse()
            );
            Assertions.assertThat(actual).isEqualTo(expected);
        }
    }
}
