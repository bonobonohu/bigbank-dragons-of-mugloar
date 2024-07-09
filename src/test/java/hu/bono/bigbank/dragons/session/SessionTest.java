package hu.bono.bigbank.dragons.session;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

class SessionTest {

    private final Session underTest = new Session(
            "gameId123",
            Instant.from(
                    DateTimeFormatter
                            .ofPattern("yyyy-MM-dd HH:mm:ss")
                            .withZone(ZoneId.of("UTC"))
                            .parse("2024-07-09 20:27:42")
            ),
            3,
            0,
            0,
            0,
            0,
            0
    );

    @Test
    void testGetLogFileNameReturnsTimestampAndGameIdInGivenFormat() {
        final String expected = "2024-07-09_202742_gameId123";
        final String actual = underTest.getLogFileName();
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}
