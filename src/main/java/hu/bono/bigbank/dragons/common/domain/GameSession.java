package hu.bono.bigbank.dragons.common.domain;

import hu.bono.bigbank.dragons.message.domain.Message;
import hu.bono.bigbank.dragons.shop.domain.ShopItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class GameSession {

    private final Instant creationTimestamp;
    private final String gameId;
    private final CharacterSheet characterSheet;
    private Integer turn;
    private List<ShopItem> shop;
    private List<Message> messages;

    public String getLogFileName() {
        final String dateTimeString = DateTimeFormatter
            .ofPattern("yyyyMMdd_HHmmss")
            .withZone(ZoneId.of("UTC"))
            .format(creationTimestamp);
        return String.format("%s_%s", dateTimeString, gameId);
    }
}
