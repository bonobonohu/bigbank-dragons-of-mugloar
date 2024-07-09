package hu.bono.bigbank.dragons.common.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

class GameSessionTest {

    private final GameSession underTest = new GameSession(
        Instant.parse("2024-07-09T20:27:42Z"),
        "GameId123",
        3,
        0,
        0,
        0,
        0,
        0
    );

    @Test
    void testGetLogFileNameReturnsTimestampAndGameIdInGivenFormat() {
        final String expected = "2024-07-09_202742_GameId123";
        final String actual = underTest.getLogFileName();
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}
