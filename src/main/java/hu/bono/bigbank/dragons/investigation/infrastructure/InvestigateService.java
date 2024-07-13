package hu.bono.bigbank.dragons.investigation.infrastructure;

import hu.bono.bigbank.dragons.common.domain.GameSession;
import hu.bono.bigbank.dragons.investigation.application.InvestigateClient;
import hu.bono.bigbank.dragons.investigation.application.PostInvestigateReputationResponse;
import hu.bono.bigbank.dragons.investigation.application.PostInvestigateReputationResponseMapper;
import hu.bono.bigbank.dragons.investigation.domain.Reputation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvestigateService {

    private final InvestigateClient investigateClient;

    public Reputation investigateReputation(
        final GameSession gameSession
    ) {
        final PostInvestigateReputationResponse postInvestigateReputationResponse =
            investigateClient.postInvestigateReputation(gameSession.getGameId());
        return PostInvestigateReputationResponseMapper.MAPPER
            .postInvestigateReputationResponseToReputation(postInvestigateReputationResponse);
    }
}
