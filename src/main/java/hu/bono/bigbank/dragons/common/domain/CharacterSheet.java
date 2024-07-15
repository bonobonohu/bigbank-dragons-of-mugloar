package hu.bono.bigbank.dragons.common.domain;

import hu.bono.bigbank.dragons.investigation.domain.Reputation;
import hu.bono.bigbank.dragons.shop.domain.ShopItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class CharacterSheet {

    private final String name;
    private Integer lives;
    private Integer gold;
    private Integer level;
    private Integer score;
    private Integer highScore;
    private Reputation reputation;
    private Set<ShopItem> purchasedItems;
    private MyBook myBook;

    @Data
    @Builder
    public static class MyBook {

        private Integer lives;
        private Integer gold;
        private Integer level;
        private Integer score;
    }
}
