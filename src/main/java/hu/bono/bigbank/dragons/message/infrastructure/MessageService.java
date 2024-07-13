package hu.bono.bigbank.dragons.message.infrastructure;

import hu.bono.bigbank.dragons.common.domain.GameSession;
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

    public List<Message> getAllMessages(
        final GameSession gameSession
    ) {
        final List<GetMessagesResponseItem> getMessagesResponseItems =
            messageClient.getMessages(gameSession.getGameId());
        return getMessagesResponseItems.stream()
            .map(GetMessagesResponseItemMapper.MAPPER::getMessagesResponseItemToMessage)
            .toList();
    }

    public MissionOutcome solveAd(
        final GameSession gameSession,
        final Message message
    ) {
        final PostSolveAdResponse postSolveAdResponse =
            messageClient.postSolveAd(gameSession.getGameId(), message.adId());
        return PostSolveAdResponseMapper.MAPPER
            .postSolveAdResponseToMissionOutcome(postSolveAdResponse);
    }
}
