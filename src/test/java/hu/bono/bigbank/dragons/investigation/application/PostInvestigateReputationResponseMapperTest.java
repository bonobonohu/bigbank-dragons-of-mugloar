package hu.bono.bigbank.dragons.investigation.application;

import hu.bono.bigbank.dragons.TestUtils;
import hu.bono.bigbank.dragons.investigation.domain.Reputation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PostInvestigateReputationResponseMapperTest {

    private final PostInvestigateReputationResponseMapper underTest = PostInvestigateReputationResponseMapper.MAPPER;

    @Test
    void testPostInvestigateReputationResponseToReputationWhenPostInvestigateReputationResponseIsNull() {
        final Reputation expected = null;
        final Reputation actual = underTest.postInvestigateReputationResponseToReputation(null);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testPostInvestigateReputationResponseToReputationWhenPostInvestigateReputationResponseIsNotNull() {
        final Reputation expected = TestUtils.createReputation(2.2, -1);
        final Reputation actual = underTest.postInvestigateReputationResponseToReputation(
            TestUtils.createPostInvestigateReputationResponse(2.2, -1)
        );
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}
