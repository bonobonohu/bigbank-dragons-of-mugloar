package hu.bono.bigbank.dragons.common.domain;

import hu.bono.bigbank.dragons.mission.domain.Message;
import hu.bono.bigbank.dragons.shop.domain.Shop;
import hu.bono.bigbank.dragons.shop.domain.ShopItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class GameSession {

    private final Instant creationTimestamp;
    private final String gameId;
    private final CharacterSheet characterSheet;
    private final Shop shop;
    private Integer turn;
    private Set<ShopItem> purchasedItems;
    private Set<Message> messages;
    private MyBook myBook;

    @Data
    @Builder
    public static class MyBook {

        private Integer turn;
    }

    public String getLogFileName() {
        final String dateTimeString = DateTimeFormatter
            .ofPattern("yyyyMMdd_HHmmss")
            .withZone(ZoneId.of("UTC"))
            .format(creationTimestamp);
        return String.format("%s_%s", dateTimeString, gameId);
    }
}
