package hu.bono.bigbank.dragons.common.domain;

import hu.bono.bigbank.dragons.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

class GameSessionTest {

    @Test
    void testGetLogFileNameReturnsTimestampAndGameIdInGivenFormat() {
        final String expected = "20240709_202742_GameId123";
        final String actual = TestUtils.createGameSession(
            Instant.parse("2024-07-09T20:27:42Z"),
            "GameId123"
        ).getLogFileName();
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}
