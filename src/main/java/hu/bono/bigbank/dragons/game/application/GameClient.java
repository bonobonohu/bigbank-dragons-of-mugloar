package hu.bono.bigbank.dragons.game.application;

import hu.bono.bigbank.dragons.common.application.ApiConfiguration;
import hu.bono.bigbank.dragons.common.domain.RestClientClientException;
import hu.bono.bigbank.dragons.common.domain.RestClientServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Component
@RequiredArgsConstructor
public class GameClient {

    private final RestClient restClient;
    private final ApiConfiguration apiConfiguration;

    public PostGameStartResponse postGameStart() {
        try {
            return restClient.post()
                .uri(
                    apiConfiguration.getBaseUrl() +
                        apiConfiguration.getEndpoints().getGameStart()
                )
                .retrieve()
                .body(PostGameStartResponse.class);
        } catch (RestClientResponseException exception) {
            if (exception.getStatusCode().is4xxClientError()) {
                throw new RestClientClientException(exception);
            } else {
                throw new RestClientServerException(exception);
            }
        }
    }
}
