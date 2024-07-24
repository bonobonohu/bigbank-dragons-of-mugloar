package hu.bono.bigbank.dragons.mission.infrastructure;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

@Component
public class Base64Decrypter implements Decrypter {

    @Override
    public String decrypt(
        final String input
    ) {
        return new String(Base64.decodeBase64(input));
    }
}
