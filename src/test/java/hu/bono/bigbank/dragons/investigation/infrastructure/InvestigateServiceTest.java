package hu.bono.bigbank.dragons.investigation.infrastructure;

import hu.bono.bigbank.dragons.TestUtils;
import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.common.infrastructure.LogWriter;
import hu.bono.bigbank.dragons.investigation.application.InvestigateClient;
import hu.bono.bigbank.dragons.investigation.application.PostInvestigateReputationResponse;
import hu.bono.bigbank.dragons.investigation.domain.Reputation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;

class InvestigateServiceTest {

    private static final GameSession GAME_SESSION = TestUtils.createGameSession(Instant.now());

    private final InvestigateClient investigateClient = Mockito.mock(InvestigateClient.class);
    private final LogWriter logWriter = Mockito.mock(LogWriter.class);
    private final InvestigateService underTest = new InvestigateService(investigateClient, logWriter);

    @BeforeEach
    void beforeEach() {
        Mockito.reset(investigateClient, logWriter);
    }

    @Test
    void testInvestigateReputation() {
        final PostInvestigateReputationResponse postInvestigateReputationResponse =
            TestUtils.createPostInvestigateReputationResponse(2.2, -1);
        final Reputation expected = TestUtils.createReputation(2.2, -1);
        Mockito.when(investigateClient.postInvestigateReputation(GAME_SESSION.getGameId()))
            .thenReturn(postInvestigateReputationResponse);
        final Reputation actual = underTest.investigateReputation(GAME_SESSION);
        Assertions.assertThat(actual).isEqualTo(expected);
        Mockito.verify(investigateClient, Mockito.times(1))
            .postInvestigateReputation(GAME_SESSION.getGameId());
        Mockito.verify(logWriter, Mockito.times(1))
            .log(
                GAME_SESSION,
                "investigateReputation",
                "Investigate reputation",
                postInvestigateReputationResponse
            );
    }
}
