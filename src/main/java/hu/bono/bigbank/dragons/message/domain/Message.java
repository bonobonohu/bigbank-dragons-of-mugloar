package hu.bono.bigbank.dragons.message.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;

@Builder
public record Message(
    String adId,
    String message,
    Integer reward,
    Integer expiresIn,
    Boolean wasEncrypted,
    Probability probability
) {

    @Getter
    public enum Probability {
        SURE_THING(1, "Sure thing"),
        PIECE_OF_CAKE(2, "Piece of cake"),
        WALK_IN_THE_PARK(3, "Walk in the park"),
        QUITE_LIKELY(4, "Quite likely"),
        RATHER_DETRIMENTAL(5, "Rather detrimental"),
        GAMBLE(6, "Gamble"),
        PLAYING_WITH_FIRE(7, "Playing with fire"),
        RISKY(8, "Risky"),
        SUICIDE_MISSION(9, "Suicide mission"),
        IMPOSSIBLE(10, "Impossible"),
        HMMM(100, "Hmmm....");

        private final Integer value;
        private final String text;

        Probability(Integer value, String text) {
            this.value = value;
            this.text = text;
        }

        public static Probability fromText(String text) {
            return Arrays.stream(Probability.values())
                .filter(level -> level.getText().equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No Probability with text " + text));
        }
    }
}
