package hu.bono.bigbank.dragons.game.infrastructure;

import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.game.application.PostGameResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

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

            final GameSession expected = new GameSession(
                Instant.from(
                    DateTimeFormatter
                        .ofPattern("yyyy-MM-dd HH:mm:ss")
                        .withZone(ZoneId.of("UTC"))
                        .parse("2024-07-09 20:27:42")
                ),
                "GameId123",
                3,
                0,
                0,
                0,
                0,
                0
            );
            final GameSession actual = underTest.postGameResponseToGameSession(
                new PostGameResponse(
                    "GameId123",
                    3,
                    0,
                    0,
                    0,
                    0,
                    0
                )
            );
            Assertions.assertThat(actual).isEqualTo(expected);
        }
    }
}
