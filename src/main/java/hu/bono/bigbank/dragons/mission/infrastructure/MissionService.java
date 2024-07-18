package hu.bono.bigbank.dragons.mission.infrastructure;

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
        final String gameId
    ) {
        final List<GetMessagesResponseItem> getMessagesResponseItems =
            missionClient.getMessages(gameId);
        return getMessagesResponseItems.stream()
            .map(getMessagesResponseItemMapper::getMessagesResponseItemToMessage)
            .toList();
    }

    public MissionOutcome goOnMission(
        final String gameId,
        final String adId
    ) {
        final PostSolveAdResponse postSolveAdResponse =
            missionClient.postSolveAd(gameId, adId);
        return PostSolveAdResponseMapper.MAPPER
            .postSolveAdResponseToMissionOutcome(postSolveAdResponse);
    }
}
