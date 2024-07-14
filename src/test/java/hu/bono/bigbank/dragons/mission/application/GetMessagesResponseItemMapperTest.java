package hu.bono.bigbank.dragons.mission.application;

import hu.bono.bigbank.dragons.TestUtils;
import hu.bono.bigbank.dragons.mission.domain.Message;
import hu.bono.bigbank.dragons.mission.infrastructure.Base64Decrypter;
import hu.bono.bigbank.dragons.mission.infrastructure.Rot13Decrypter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class GetMessagesResponseItemMapperTest {

    private final Base64Decrypter base64Decrypter = new Base64Decrypter();
    private final Rot13Decrypter rot13Decrypter = new Rot13Decrypter();
    private final GetMessagesResponseItemMapper underTest =
        new GetMessagesResponseItemMapper(base64Decrypter, rot13Decrypter);

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
    void testGetMessagesResponseItemToMessageWhenGetMessagesResponseItemIsEncryptedWithBase64() {
        final Message expected = TestUtils.createMessage(
            "uaLjgkri",
            "Infiltrate The Eagle Soldiers and recover their secrets.",
            1,
            Message.Probability.PLAYING_WITH_FIRE
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

    @Test
    void testGetMessagesResponseItemToMessageWhenGetMessagesResponseItemIsEncryptedWithRot13() {
        final Message expected = TestUtils.createMessage(
            "0P7wqVjD",
            "Kill Francis Kingston with turnips and make Candice Derrickson"
                + " from mystery island in Fallfair to take the blame",
            2,
            Message.Probability.IMPOSSIBLE
        );
        final Message actual = underTest.getMessagesResponseItemToMessage(
            TestUtils.createGetMessagesResponseItem(
                "0C7jdIwQ",
                "Xvyy Senapvf Xvatfgba jvgu gheavcf naq znxr Pnaqvpr Qreevpxfba"
                    + " sebz zlfgrel vfynaq va Snyysnve gb gnxr gur oynzr",
                "Vzcbffvoyr",
                2
            )
        );
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}
