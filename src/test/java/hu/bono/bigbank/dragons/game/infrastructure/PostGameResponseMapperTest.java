package hu.bono.bigbank.dragons.game.infrastructure;

import hu.bono.bigbank.dragons.TestUtils;
import hu.bono.bigbank.dragons.common.domain.GameSession;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.Instant;

import static org.mockito.Mockito.mockStatic;

class PostGameResponseMapperTest {

    private final PostGameResponseMapper underTest = PostGameResponseMapper.MAPPER;

    @Test
    void testPostGameResponseToSessionWhenPostGameResponseIsNull() {
        final GameSession expected = null;
        final GameSession actual = underTest.postGameResponseToGameSession(null);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testPostGameResponseToSessionWhenPostGameResponseIsNotNull() {
        try (MockedStatic<Instant> mockedInstant = mockStatic(Instant.class)) {
            final Instant fixedInstant = Instant.parse("2024-07-09T20:27:42Z");
            mockedInstant.when(Instant::now).thenReturn(fixedInstant);

            final GameSession expected = TestUtils.createGameSession(fixedInstant);
            final GameSession actual = underTest.postGameResponseToGameSession(
                TestUtils.createPostGameResponse()
            );
            Assertions.assertThat(actual).isEqualTo(expected);
        }
    }
}
