package hu.bono.bigbank.dragons.message.application;

import hu.bono.bigbank.dragons.TestUtils;
import hu.bono.bigbank.dragons.message.domain.MissionOutcome;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PostSolveAdResponseMapperTest {

    private final PostSolveAdResponseMapper underTest = PostSolveAdResponseMapper.MAPPER;

    @Test
    void testPostSolveAdResponseToMissionOutcomeWhenPostSolveAdResponseIsNull() {
        final MissionOutcome expected = null;
        final MissionOutcome actual = underTest.postSolveAdResponseToMissionOutcome(null);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testPostSolveAdResponseToMissionOutcomeWhenPostSolveAdResponseIsNotNull() {
        final MissionOutcome expected = TestUtils.createMissionOutcome(
            true,
            3,
            100,
            200,
            42
        );
        final MissionOutcome actual = underTest.postSolveAdResponseToMissionOutcome(
            TestUtils.createPostSolveAdResponse(
                true,
                3,
                100,
                200,
                42
            )
        );
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}
