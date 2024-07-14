package hu.bono.bigbank.dragons.mission.infrastructure;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class Rot13DecrypterTest {

    private final Rot13Decrypter underTest = new Rot13Decrypter();

    @Test
    void testDecrypt() {
        final String expected = "Impossible";
        final String actual = underTest.decrypt("Vzcbffvoyr");
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}
