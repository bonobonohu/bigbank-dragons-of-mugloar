package hu.bono.bigbank.dragons.mission.infrastructure;

import org.springframework.stereotype.Component;

@Component
public class Rot13Decrypter implements Decrypter {

    @Override
    public String decrypt(
        final String input
    ) {
        final StringBuilder result = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                c = (char) ((c - 'a' + 13) % 26 + 'a');
            } else if (c >= 'A' && c <= 'Z') {
                c = (char) ((c - 'A' + 13) % 26 + 'A');
            }
            result.append(c);
        }
        return result.toString();
    }

}
