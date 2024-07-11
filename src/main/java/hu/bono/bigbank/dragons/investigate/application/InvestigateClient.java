package hu.bono.bigbank.dragons.investigate.application;

import hu.bono.bigbank.dragons.common.application.ApiConfiguration;
import hu.bono.bigbank.dragons.common.domain.RestClientClientException;
import hu.bono.bigbank.dragons.common.domain.RestClientServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Component
@RequiredArgsConstructor
public class InvestigateClient {

    private final RestClient restClient;
    private final ApiConfiguration apiConfiguration;

    static String prepareUri(String endpointTemplate, String gameId) {
        return endpointTemplate.replace("{gameId}", gameId);
    }

    public PostInvestigateReputationResponse postInvestigateReputation(String gameId) {
        try {
            return restClient.post()
                .uri(
                    apiConfiguration.getBaseUrl() +
                        prepareUri(apiConfiguration.getEndpoints().getInvestigateReputation(), gameId)
                )
                .retrieve()
                .body(PostInvestigateReputationResponse.class);
        } catch (RestClientResponseException exception) {
            if (exception.getStatusCode().is4xxClientError()) {
                throw new RestClientClientException(exception);
            } else {
                throw new RestClientServerException(exception);
            }
        }
    }
}
