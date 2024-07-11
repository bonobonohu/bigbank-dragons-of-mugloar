package hu.bono.bigbank.dragons.investigate.infrastructure;

import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.common.infrastructure.LogWriter;
import hu.bono.bigbank.dragons.investigate.application.InvestigateClient;
import hu.bono.bigbank.dragons.investigate.application.PostInvestigateReputationResponse;
import hu.bono.bigbank.dragons.investigate.application.PostInvestigateReputationResponseMapper;
import hu.bono.bigbank.dragons.investigate.domain.Reputation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvestigateService {

    private final InvestigateClient investigateClient;
    private final LogWriter logWriter;

    public Reputation investigateReputation(GameSession gameSession) {
        final PostInvestigateReputationResponse postInvestigateReputationResponse =
            investigateClient.postInvestigateReputation(gameSession.getGameId());
        final Reputation reputation = PostInvestigateReputationResponseMapper.MAPPER
            .postInvestigateReputationResponseToReputation(postInvestigateReputationResponse);
        logWriter.log(
            gameSession,
            "investigateReputation",
            "Investigate reputation",
            postInvestigateReputationResponse
        );
        return reputation;
    }
}
