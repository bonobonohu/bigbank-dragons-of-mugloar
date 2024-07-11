package hu.bono.bigbank.dragons.message.infrastructure;

import hu.bono.bigbank.dragons.TestUtils;
import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.common.infrastructure.LogWriter;
import hu.bono.bigbank.dragons.message.application.GetMessagesResponseItem;
import hu.bono.bigbank.dragons.message.application.MessagesClient;
import hu.bono.bigbank.dragons.message.domain.Message;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.List;

class MessageServiceTest {

    private static final GameSession GAME_SESSION = TestUtils.createGameSession(Instant.now());

    private final MessagesClient MessagesClient = Mockito.mock(MessagesClient.class);
    private final LogWriter logWriter = Mockito.mock(LogWriter.class);
    private final MessageService underTest = new MessageService(MessagesClient, logWriter);

    @BeforeEach
    void beforeEach() {
        Mockito.reset(MessagesClient, logWriter);
    }

    @Test
    void testInvestigateReputation() {
        final List<GetMessagesResponseItem> getMessagesResponseItems =
            TestUtils.createGetMessagesResponseItems();
        final List<Message> expected = TestUtils.createMessages();
        Mockito.when(MessagesClient.getMessages(GAME_SESSION.getGameId()))
            .thenReturn(getMessagesResponseItems);
        final List<Message> actual = underTest.getAllMessages(GAME_SESSION);
        Assertions.assertThat(actual).isEqualTo(expected);
        Mockito.verify(MessagesClient, Mockito.times(1))
            .getMessages(GAME_SESSION.getGameId());
        Mockito.verify(logWriter, Mockito.times(1))
            .log(
                GAME_SESSION,
                "getAllMessages",
                "Get all messages",
                getMessagesResponseItems
            );
    }
}
