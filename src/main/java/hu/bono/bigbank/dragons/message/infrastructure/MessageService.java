package hu.bono.bigbank.dragons.message.infrastructure;

import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.common.infrastructure.LogWriter;
import hu.bono.bigbank.dragons.message.application.*;
import hu.bono.bigbank.dragons.message.domain.Message;
import hu.bono.bigbank.dragons.message.domain.MissionOutcome;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageClient messageClient;
    private final LogWriter logWriter;

    public List<Message> getAllMessages(GameSession gameSession) {
        final List<GetMessagesResponseItem> getMessagesResponseItems =
            messageClient.getMessages(gameSession.getGameId());
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

    public MissionOutcome solveAd(GameSession gameSession, Message message) {
        final PostSolveAdResponse PostSolveAdResponse =
            messageClient.postSolveAd(gameSession.getGameId(), message.adId());
        final MissionOutcome MissionOutcome = PostSolveAdResponseMapper.MAPPER
            .postSolveAdResponseToMissionOutcome(PostSolveAdResponse);
        logWriter.log(
            gameSession,
            "solveAd",
            "Solve ad",
            PostSolveAdResponse
        );
        return MissionOutcome;
    }
}
