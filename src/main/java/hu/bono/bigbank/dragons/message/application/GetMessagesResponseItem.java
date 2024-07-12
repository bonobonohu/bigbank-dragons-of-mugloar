package hu.bono.bigbank.dragons.message.application;

import lombok.Builder;
import org.apache.commons.codec.binary.Base64;

@Builder
public record GetMessagesResponseItem(
    String adId,
    String message,
    Integer reward,
    Integer expiresIn,
    Integer encrypted,
    String probability
) {

    public boolean isEncrypted() {
        return encrypted != null && encrypted == 1;
    }

    public String decryptedAdId() {
        return isEncrypted() ? new String(Base64.decodeBase64(adId)) : adId;
    }

    public String decryptedMessage() {
        return isEncrypted() ? new String(Base64.decodeBase64(message)) : message;
    }

    public String decryptedProbability() {
        return isEncrypted() ? new String(Base64.decodeBase64(probability)) : probability;
    }
}
