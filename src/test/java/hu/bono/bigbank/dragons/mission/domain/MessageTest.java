package hu.bono.bigbank.dragons.mission.domain;

import hu.bono.bigbank.dragons.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

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

    @ParameterizedTest
    @MethodSource(
        "possiblyIncreasesPeopleReputation"
    )
    void testPossiblyIncreasesPeopleReputation(
        final String message,
        final boolean expected
    ) {
        final boolean actual = TestUtils.createMessage(
                "AdId123",
                message,
                Message.Probability.PIECE_OF_CAKE)
            .possiblyIncreasesPeopleReputation();
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> possiblyIncreasesPeopleReputation() {
        return Stream.of(
            Arguments.of(
                "Escort Modestine Underhill to savannah in Razorwhyte where they can meet with their long lost cat",
                false),
            Arguments.of(
                "Help Tarquinius Reynell to fix their bucket",
                false),
            Arguments.of(
                "Steal super awesome diamond water from Pomona Gardener",
                false),
            Arguments.of(
                "Help defending peninsula in Newmere from the intruders",
                true),
            Arguments.of(
                "Steal squirrel delivery to Lysanne Prescott and share some of the profits with the people.",
                true)
        );
    }

    @ParameterizedTest
    @MethodSource(
        "possiblyIncreasesStateReputation"
    )
    void testPossiblyIncreasesStateReputation(
        final String message,
        final boolean expected
    ) {
        final boolean actual = TestUtils.createMessage(
                "AdId123",
                message,
                Message.Probability.PIECE_OF_CAKE)
            .possiblyIncreasesStateReputation();
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> possiblyIncreasesStateReputation() {
        return Stream.of(
            Arguments.of(
                "Escort Modestine Underhill to savannah in Razorwhyte where they can meet with their long lost cat",
                false),
            Arguments.of(
                "Help Tarquinius Reynell to fix their bucket",
                false),
            Arguments.of(
                "Help defending peninsula in Newmere from the intruders",
                true)
        );
    }

    @ParameterizedTest
    @MethodSource(
        "possiblyDecreasesStateReputation"
    )
    void testPossiblyDecreasesStateReputation(
        final String message,
        final boolean expected
    ) {
        final boolean actual = TestUtils.createMessage(
                "AdId123",
                message,
                Message.Probability.PIECE_OF_CAKE)
            .possiblyDecreasesStateReputation();
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> possiblyDecreasesStateReputation() {
        return Stream.of(
            Arguments.of(
                "Escort Modestine Underhill to savannah in Razorwhyte where they can meet with their long lost cat",
                false),
            Arguments.of(
                "Help Tarquinius Reynell to fix their bucket",
                false),
            Arguments.of(
                "Help defending peninsula in Newmere from the intruders",
                false),
            Arguments.of(
                "Steal super awesome diamond water from Pomona Gardener",
                true),
            Arguments.of(
                "Steal squirrel delivery to Lysanne Prescott and share some of the profits with the people.",
                true),
            Arguments.of(
                "Help Madelief Francis to sell an unordinary weed on the local market",
                true)
        );
    }

    @ParameterizedTest
    @MethodSource(
        "itsATrap"
    )
    void testItsATrap(
        final String message,
        final boolean expected
    ) {
        final boolean actual = TestUtils.createMessage(
                "AdId123",
                message,
                Message.Probability.PIECE_OF_CAKE)
            .itsATrap();
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> itsATrap() {
        return Stream.of(
            Arguments.of(
                "Escort Modestine Underhill to savannah in Razorwhyte where they can meet with their long lost cat",
                false),
            Arguments.of(
                "Help Tarquinius Reynell to fix their bucket",
                false),
            Arguments.of(
                "Help defending peninsula in Newmere from the intruders",
                false),
            Arguments.of(
                "Steal squirrel delivery to Lysanne Prescott and share some of the profits with the people.",
                false),
            Arguments.of(
                "Steal super awesome diamond squirrel from Boróka Freeman",
                true),
            Arguments.of(
                "Steal super awesome diamond water from Pomona Gardener",
                true)
        );
    }
}
