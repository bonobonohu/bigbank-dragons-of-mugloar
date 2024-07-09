package hu.bono.bigbank.dragons.session;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
public class Session {

    private final String gameId;
    private final Instant timestamp;
    private Integer lives;
    private Integer gold;
    private Integer level;
    private Integer score;
    private Integer highScore;
    private Integer turn;

    public String getLogFileName() {
        final String dateTimeString = DateTimeFormatter
                .ofPattern("yyyy-MM-dd_HHmmss")
                .withZone(ZoneId.of("UTC"))
                .format(timestamp);
        return String.format("%s_%s", dateTimeString, gameId);
    }
}
