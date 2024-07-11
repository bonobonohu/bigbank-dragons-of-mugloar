package hu.bono.bigbank.dragons.message.infrastructure;

import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.common.infrastructure.LogWriter;
import hu.bono.bigbank.dragons.message.application.GetMessagesResponseItem;
import hu.bono.bigbank.dragons.message.application.GetMessagesResponseItemMapper;
import hu.bono.bigbank.dragons.message.application.MessagesClient;
import hu.bono.bigbank.dragons.message.domain.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessagesClient messagesClient;
    private final LogWriter logWriter;

    public List<Message> getAllMessages(GameSession gameSession) {
        final List<GetMessagesResponseItem> getMessagesResponseItems =
            messagesClient.getMessages(gameSession.getGameId());
        final List<Message> messages = getMessagesResponseItems.stream()
            .map(GetMessagesResponseItemMapper.MAPPER::getMessagesResponseItemToMessage)
            .toList();
        logWriter.log(
            gameSession,
            "getAllMessages",
            "Get all messages",
            getMessagesResponseItems
        );
        return messages;
    }
}
