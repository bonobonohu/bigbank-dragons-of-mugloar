package hu.bono.bigbank.dragons.investigation.infrastructure;

import hu.bono.bigbank.dragons.TestUtils;
import hu.bono.bigbank.dragons.investigation.application.InvestigateClient;
import hu.bono.bigbank.dragons.investigation.application.PostInvestigateReputationResponse;
import hu.bono.bigbank.dragons.investigation.domain.Reputation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class InvestigateServiceTest {

    private static final String GAME_ID = "GameId123";

    private final InvestigateClient investigateClient = Mockito.mock(InvestigateClient.class);
    private final InvestigateService underTest = new InvestigateService(investigateClient);

    @BeforeEach
    void beforeEach() {
        Mockito.reset(investigateClient);
    }

    @Test
    void testInvestigateReputation() {
        final PostInvestigateReputationResponse postInvestigateReputationResponse =
            TestUtils.createPostInvestigateReputationResponse(2.2, -1);
        final Reputation expected = TestUtils.createReputation(2.2, -1);
        Mockito.when(investigateClient.postInvestigateReputation(GAME_ID))
            .thenReturn(postInvestigateReputationResponse);
        final Reputation actual = underTest.investigateReputation(GAME_ID);
        Assertions.assertThat(actual).isEqualTo(expected);
        Mockito.verify(investigateClient)
            .postInvestigateReputation(GAME_ID);
    }
}
