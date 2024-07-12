package hu.bono.bigbank.dragons.message.application;

import hu.bono.bigbank.dragons.message.domain.Message;
import org.apache.commons.codec.binary.Base64;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(
    imports = {Base64.class, Message.Probability.class}
)
public interface GetMessagesResponseItemMapper {

    GetMessagesResponseItemMapper MAPPER = Mappers.getMapper(GetMessagesResponseItemMapper.class);

    @Mapping(
        target = "adId",
        expression = "java(getMessagesResponseItem.decryptedAdId())"
    )
    @Mapping(
        target = "message",
        expression = "java(getMessagesResponseItem.decryptedMessage())"
    )
    @Mapping(
        target = "wasEncrypted",
        expression = "java(getMessagesResponseItem.isEncrypted())"
    )
    @Mapping(
        target = "probability",
        expression = "java(Message.Probability.fromText(getMessagesResponseItem.decryptedProbability()))"
    )
    Message getMessagesResponseItemToMessage(
        GetMessagesResponseItem getMessagesResponseItem
    );
}
