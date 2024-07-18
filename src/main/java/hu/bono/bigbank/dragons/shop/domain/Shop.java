package hu.bono.bigbank.dragons.shop.domain;

import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class Shop {

    private final Map<String, ShopItem> items = new HashMap<>();

    public Set<ShopItem> getItemsAsSet() {
        return new HashSet<>(items.values());
    }

    public int getLevelUpItemsCount() {
        return (int) items.values().stream()
            .filter(item -> ShopItem.LEVEL_UP_ITEM_COSTS.contains(item.cost()))
            .count();
    }

    public Shop setItems(
        final Map<String, ShopItem> items
    ) {
        this.items.putAll(items);
        return this;
    }

    public Shop setItems(
        final Collection<ShopItem> items
    ) {
        return this.setItems(
            items.stream()
                .collect(
                    Collectors.toMap(
                        ShopItem::id,
                        item -> item
                    )
                )
        );
    }

    public ShopItem getHPot() {
        return Optional.ofNullable(items.get(ShopItem.HEALING_POT_ID))
            .orElseThrow(() ->
                new RuntimeException("No Healing potion in Shop")
            );
    }
}
