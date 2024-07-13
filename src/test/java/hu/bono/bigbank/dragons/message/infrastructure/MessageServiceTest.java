package hu.bono.bigbank.dragons.message.infrastructure;

import hu.bono.bigbank.dragons.TestUtils;
import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.message.application.GetMessagesResponseItem;
import hu.bono.bigbank.dragons.message.application.MessageClient;
import hu.bono.bigbank.dragons.message.application.PostSolveAdResponse;
import hu.bono.bigbank.dragons.message.domain.Message;
import hu.bono.bigbank.dragons.message.domain.MissionOutcome;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.List;

class MessageServiceTest {

    private static final GameSession GAME_SESSION = TestUtils.createGameSession(Instant.now());
    private static final Message MESSAGE = TestUtils.createMessage();

    private final MessageClient messageClient = Mockito.mock(MessageClient.class);
    private final MessageService underTest = new MessageService(messageClient);

    @BeforeEach
    void beforeEach() {
        Mockito.reset(messageClient);
    }

    @Test
    void testGetAllMessages() {
        final List<GetMessagesResponseItem> getMessagesResponseItems = TestUtils.createGetMessagesResponseItems();
        final List<Message> expected = TestUtils.createMessages();
        Mockito.when(messageClient.getMessages(GAME_SESSION.getGameId()))
            .thenReturn(getMessagesResponseItems);
        final List<Message> actual = underTest.getAllMessages(GAME_SESSION);
        Assertions.assertThat(actual).isEqualTo(expected);
        Mockito.verify(messageClient)
            .getMessages(GAME_SESSION.getGameId());
    }

    @Test
    void testSolveAd() {
        final PostSolveAdResponse postSolveAdResponse =
            TestUtils.createPostSolveAdResponse(true, 3, 100, 200, 42);
        final MissionOutcome expected =
            TestUtils.createMissionOutcome(true, 3, 100, 200, 42);
        Mockito.when(messageClient.postSolveAd(GAME_SESSION.getGameId(), MESSAGE.adId()))
            .thenReturn(postSolveAdResponse);
        final MissionOutcome actual = underTest.solveAd(GAME_SESSION, MESSAGE);
        Assertions.assertThat(actual).isEqualTo(expected);
        Mockito.verify(messageClient)
            .postSolveAd(GAME_SESSION.getGameId(), MESSAGE.adId());
    }
}
