package hu.bono.bigbank.dragons.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@Builder
public class GameSession {

    private final Instant creationTimestamp;
    private final String gameId;
    private Integer lives;
    private Integer gold;
    private Integer level;
    private Integer score;
    private Integer highScore;
    private Integer turn;

    public String getLogFileName() {
        final String dateTimeString = DateTimeFormatter
            .ofPattern("yyyyMMdd_HHmmss")
            .withZone(ZoneId.of("UTC"))
            .format(creationTimestamp);
        return String.format("%s_%s", dateTimeString, gameId);
    }
}
