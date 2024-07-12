package hu.bono.bigbank.dragons.message.application;

import hu.bono.bigbank.dragons.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class GetMessagesResponseItemTest {

    @ParameterizedTest
    @MethodSource(
        "provideStringsForIsBlank"
    )
    void testIsEncrypted(
        final Integer encrypted,
        final Boolean expected
    ) {
        final Boolean actual = TestUtils.createGetMessagesResponseItem("", "", "", encrypted)
            .isEncrypted();
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> provideStringsForIsBlank() {
        return Stream.of(
            Arguments.of(null, false),
            Arguments.of(0, false),
            Arguments.of(2, false),
            Arguments.of(1, true)
        );
    }

    @Test
    void testValuesAreDecrypted() {
        final GetMessagesResponseItem actual = TestUtils.createGetMessagesResponseItem(
            "dWFMamdrcmk=",
            "SW5maWx0cmF0ZSBUaGUgRWFnbGUgU29sZGllcnMgYW5kIHJlY292ZXIgdGhlaXIgc2VjcmV0cy4=",
            "UGxheWluZyB3aXRoIGZpcmU=",
            1
        );
        Assertions.assertThat(actual.decryptedAdId())
            .isEqualTo("uaLjgkri");
        Assertions.assertThat(actual.decryptedMessage())
            .isEqualTo("Infiltrate The Eagle Soldiers and recover their secrets.");
        Assertions.assertThat(actual.decryptedProbability())
            .isEqualTo("Playing with fire");
    }
}
