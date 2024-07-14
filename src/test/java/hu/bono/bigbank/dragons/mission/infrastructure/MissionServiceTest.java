package hu.bono.bigbank.dragons.mission.infrastructure;

import hu.bono.bigbank.dragons.TestUtils;
import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.mission.application.GetMessagesResponseItem;
import hu.bono.bigbank.dragons.mission.application.GetMessagesResponseItemMapper;
import hu.bono.bigbank.dragons.mission.application.MissionClient;
import hu.bono.bigbank.dragons.mission.application.PostSolveAdResponse;
import hu.bono.bigbank.dragons.mission.domain.Message;
import hu.bono.bigbank.dragons.mission.domain.MissionOutcome;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.List;

class MissionServiceTest {

    private static final GameSession GAME_SESSION = TestUtils.createGameSession(Instant.now());
    private static final Message MESSAGE = TestUtils.createMessage();

    private final MissionClient missionClient = Mockito.mock(MissionClient.class);
    private final GetMessagesResponseItemMapper getMessagesResponseItemMapper =
        Mockito.mock(GetMessagesResponseItemMapper.class);
    private final MissionService underTest = new MissionService(missionClient, getMessagesResponseItemMapper);

    @BeforeEach
    void beforeEach() {
        Mockito.reset(missionClient);
    }

    @Test
    void testGetMessages() {
        final List<GetMessagesResponseItem> getMessagesResponseItems = TestUtils.createGetMessagesResponseItems();
        final List<Message> expected = TestUtils.createMessages();
        Mockito.when(missionClient.getMessages(GAME_SESSION.getGameId()))
            .thenReturn(getMessagesResponseItems);
        Mockito.when(getMessagesResponseItemMapper.getMessagesResponseItemToMessage(getMessagesResponseItems.get(0)))
            .thenReturn(expected.get(0));
        Mockito.when(getMessagesResponseItemMapper.getMessagesResponseItemToMessage(getMessagesResponseItems.get(1)))
            .thenReturn(expected.get(1));
        Mockito.when(getMessagesResponseItemMapper.getMessagesResponseItemToMessage(getMessagesResponseItems.get(2)))
            .thenReturn(expected.get(2));
        final List<Message> actual = underTest.getMessages(GAME_SESSION);
        Assertions.assertThat(actual).isEqualTo(expected);
        Mockito.verify(missionClient)
            .getMessages(GAME_SESSION.getGameId());
    }

    @Test
    void testGoOnMission() {
        final PostSolveAdResponse postSolveAdResponse =
            TestUtils.createPostSolveAdResponse(true, 3, 100, 200, 42);
        final MissionOutcome expected =
            TestUtils.createMissionOutcome(true, 3, 100, 200, 42);
        Mockito.when(missionClient.postSolveAd(GAME_SESSION.getGameId(), MESSAGE.adId()))
            .thenReturn(postSolveAdResponse);
        final MissionOutcome actual = underTest.goOnMission(GAME_SESSION, MESSAGE);
        Assertions.assertThat(actual).isEqualTo(expected);
        Mockito.verify(missionClient)
            .postSolveAd(GAME_SESSION.getGameId(), MESSAGE.adId());
    }
}
