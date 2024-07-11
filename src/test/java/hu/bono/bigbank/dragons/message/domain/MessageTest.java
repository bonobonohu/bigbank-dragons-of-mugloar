package hu.bono.bigbank.dragons.message.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MessageTest {

    @Test
    void testMessageProbabilityFromTextThrowsIllegalArgumentExceptionWhenEnumValueCannotBeFound() {
        Assertions.assertThatThrownBy(
                () -> Message.Probability.fromText("You don't wanna do this!")
            )
            .isInstanceOf(IllegalArgumentException.class)
            .message().isEqualTo("No Probability with text You don't wanna do this!");
    }

    @Test
    void testMessageProbabilityFromTextReturnsEnumValueWhenItCanBeFoundByText() {
        final Message.Probability expected = Message.Probability.WALK_IN_THE_PARK;
        final Message.Probability actual = Message.Probability.fromText("Walk in the park");
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}
