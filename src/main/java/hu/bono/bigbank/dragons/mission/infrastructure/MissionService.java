package hu.bono.bigbank.dragons.mission.infrastructure;

import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.mission.application.*;
import hu.bono.bigbank.dragons.mission.domain.Message;
import hu.bono.bigbank.dragons.mission.domain.MissionOutcome;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionClient missionClient;
    private final GetMessagesResponseItemMapper getMessagesResponseItemMapper;

    public List<Message> getMessages(
        final GameSession gameSession
    ) {
        final List<GetMessagesResponseItem> getMessagesResponseItems =
            missionClient.getMessages(gameSession.getGameId());
        return getMessagesResponseItems.stream()
            .map(getMessagesResponseItemMapper::getMessagesResponseItemToMessage)
            .toList();
    }

    public MissionOutcome goOnMission(
        final GameSession gameSession,
        final Message message
    ) {
        final PostSolveAdResponse postSolveAdResponse =
            missionClient.postSolveAd(gameSession.getGameId(), message.adId());
        return PostSolveAdResponseMapper.MAPPER
            .postSolveAdResponseToMissionOutcome(postSolveAdResponse);
    }
}
