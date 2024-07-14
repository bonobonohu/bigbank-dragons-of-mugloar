package hu.bono.bigbank.dragons.mission.application;

import hu.bono.bigbank.dragons.common.application.ApiConfiguration;
import hu.bono.bigbank.dragons.common.domain.RestClientClientException;
import hu.bono.bigbank.dragons.common.domain.RestClientServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MissionClient {

    private final RestClient restClient;
    private final ApiConfiguration apiConfiguration;

    static String prepareUri(
        final String endpointTemplate,
        final String gameId
    ) {
        return endpointTemplate.replace("{gameId}", gameId);
    }

    static String prepareUri(
        final String endpointTemplate,
        final String gameId,
        final String adId
    ) {
        return endpointTemplate
            .replace("{gameId}", gameId)
            .replace("{adId}", adId);
    }

    public List<GetMessagesResponseItem> getMessages(
        final String gameId
    ) {
        try {
            return restClient.get()
                .uri(
                    apiConfiguration.getBaseUrl()
                        + prepareUri(apiConfiguration.getEndpoints().getMessages(), gameId)
                )
                .retrieve()
                .body(
                    new ParameterizedTypeReference<>() {
                    });
        } catch (RestClientResponseException exception) {
            if (exception.getStatusCode().is4xxClientError()) {
                throw new RestClientClientException(exception);
            } else {
                throw new RestClientServerException(exception);
            }
        }
    }

    public PostSolveAdResponse postSolveAd(
        final String gameId,
        final String adId
    ) {
        try {
            return restClient.post()
                .uri(
                    apiConfiguration.getBaseUrl()
                        + prepareUri(apiConfiguration.getEndpoints().getSolveAd(), gameId, adId)
                )
                .retrieve()
                .body(PostSolveAdResponse.class);
        } catch (RestClientResponseException exception) {
            if (exception.getStatusCode().is4xxClientError()) {
                throw new RestClientClientException(exception);
            } else {
                throw new RestClientServerException(exception);
            }
        }
    }
}
