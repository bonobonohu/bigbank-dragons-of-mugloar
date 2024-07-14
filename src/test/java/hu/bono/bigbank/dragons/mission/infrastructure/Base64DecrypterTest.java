package hu.bono.bigbank.dragons.mission.infrastructure;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Base64;

class Base64DecrypterTest {

    private final Base64Decrypter underTest = new Base64Decrypter();

    @Test
    void testDecrypt() {
        final String expected = "Williams";
        final String actual = underTest.decrypt(Base64.getEncoder().encodeToString(expected.getBytes()));
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}
