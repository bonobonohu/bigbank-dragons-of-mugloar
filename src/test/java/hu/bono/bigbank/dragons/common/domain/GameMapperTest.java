package hu.bono.bigbank.dragons.common.domain;

import hu.bono.bigbank.dragons.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.Instant;

import static org.mockito.Mockito.mockStatic;

class GameMapperTest {

    private final GameMapper underTest = new GameMapper();

    @Test
    void testGameToGameSessionWhenBothGameAndCharacterSheetAreNull() {
        final GameSession actual = underTest.gameToGameSession(
            null,
            null
        );
        Assertions.assertThat(actual).isNull();
    }

    @Test
    void testGameToGameSessionWhenGameIsNull() {
        final GameSession actual = underTest.gameToGameSession(
            null,
            TestUtils.createCharacterSheet()
        );
        Assertions.assertThat(actual).isNull();
    }

    @Test
    void testGameToGameSessionWhenCharacterSheetIsNull() {
        final GameSession actual = underTest.gameToGameSession(
            TestUtils.createGame(),
            null
        );
        Assertions.assertThat(actual).isNull();
    }

    @Test
    void testGameToGameSessionWhenGameIsNotNull() {
        try (MockedStatic<Instant> mockedInstant = mockStatic(Instant.class)) {
            final Instant fixedInstant = Instant.parse("2024-07-09T20:27:42Z");
            mockedInstant.when(Instant::now).thenReturn(fixedInstant);
            final GameSession expected = TestUtils.createGameSession(fixedInstant);
            final GameSession actual = underTest.gameToGameSession(
                TestUtils.createGame(),
                TestUtils.createCharacterSheet()
            );
            Assertions.assertThat(actual).isEqualTo(expected);
        }
    }
}
