package hu.bono.bigbank.dragons.mission.application;

import hu.bono.bigbank.dragons.mission.domain.Message;
import hu.bono.bigbank.dragons.mission.infrastructure.Base64Decrypter;
import hu.bono.bigbank.dragons.mission.infrastructure.Rot13Decrypter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetMessagesResponseItemMapper {

    private final Base64Decrypter base64Decrypter;
    private final Rot13Decrypter rot13Decrypter;

    public Message getMessagesResponseItemToMessage(
        final GetMessagesResponseItem getMessagesResponseItem
    ) {
        if (getMessagesResponseItem == null) {
            return null;
        }
        final Message.MessageBuilder messageBuilder =
            Message.builder()
                .reward(getMessagesResponseItem.reward())
                .expiresIn(getMessagesResponseItem.expiresIn())
                .encrypted(getMessagesResponseItem.encrypted());
        return buildDecrypted(messageBuilder, getMessagesResponseItem);
    }

    private Message buildDecrypted(
        final Message.MessageBuilder messageBuilder,
        final GetMessagesResponseItem getMessagesResponseItem
    ) {
        return switch (getMessagesResponseItem.encrypted()) {
            case null -> withoutDecrypt(messageBuilder, getMessagesResponseItem);
            case 1 -> decryptWithBase64(messageBuilder, getMessagesResponseItem);
            case 2 -> decryptWithRot13(messageBuilder, getMessagesResponseItem);
            default -> throw new RuntimeException("Unknown encryption");
        };
    }

    private Message withoutDecrypt(
        final Message.MessageBuilder messageBuilder,
        final GetMessagesResponseItem getMessagesResponseItem
    ) {
        return messageBuilder
            .adId(getMessagesResponseItem.adId())
            .message(getMessagesResponseItem.message())
            .probability(Message.Probability.fromText(getMessagesResponseItem.probability()))
            .build();
    }

    private Message decryptWithBase64(
        final Message.MessageBuilder messageBuilder,
        final GetMessagesResponseItem getMessagesResponseItem
    ) {
        return messageBuilder
            .adId(base64Decrypter.decrypt(getMessagesResponseItem.adId()))
            .message(base64Decrypter.decrypt(getMessagesResponseItem.message()))
            .probability(Message.Probability.fromText(base64Decrypter.decrypt(getMessagesResponseItem.probability())))
            .build();
    }

    private Message decryptWithRot13(
        final Message.MessageBuilder messageBuilder,
        final GetMessagesResponseItem getMessagesResponseItem
    ) {
        return messageBuilder
            .adId(rot13Decrypter.decrypt(getMessagesResponseItem.adId()))
            .message(rot13Decrypter.decrypt(getMessagesResponseItem.message()))
            .probability(Message.Probability.fromText(rot13Decrypter.decrypt(getMessagesResponseItem.probability())))
            .build();
    }
}
