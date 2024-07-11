package hu.bono.bigbank.dragons.message.application;

import hu.bono.bigbank.dragons.TestUtils;
import hu.bono.bigbank.dragons.message.domain.Message;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class GetMessagesResponseItemMapperTest {

    private final GetMessagesResponseItemMapper underTest = GetMessagesResponseItemMapper.MAPPER;

    @Test
    void testGetMessagesResponseItemToMessageWhenGetMessagesResponseItemIsNull() {
        final Message actual = underTest.getMessagesResponseItemToMessage(null);
        Assertions.assertThat(actual).isNull();
    }

    @Test
    void testGetMessagesResponseItemToMessageWhenGetMessagesResponseItemIsPlain() {
        final Message expected = TestUtils.createMessage(
            "AdId123",
            "Help Tarquinius Reynell to fix their bucket",
            Message.Probability.PIECE_OF_CAKE
        );
        final Message actual = underTest.getMessagesResponseItemToMessage(
            TestUtils.createGetMessagesResponseItem(
                "AdId123",
                "Help Tarquinius Reynell to fix their bucket",
                "Piece of cake"
            )
        );
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testGetMessagesResponseItemToMessageWhenGetMessagesResponseItemIsEncrypted() {
        final Message expected = TestUtils.createMessage(
            "uaLjgkri",
            "Infiltrate The Eagle Soldiers and recover their secrets.",
            Message.Probability.PLAYING_WITH_FIRE,
            true
        );
        final Message actual = underTest.getMessagesResponseItemToMessage(
            TestUtils.createGetMessagesResponseItem(
                "dWFMamdrcmk=",
                "SW5maWx0cmF0ZSBUaGUgRWFnbGUgU29sZGllcnMgYW5kIHJlY292ZXIgdGhlaXIgc2VjcmV0cy4=",
                "UGxheWluZyB3aXRoIGZpcmU=",
                1
            )
        );
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}
