package hu.bono.bigbank.dragons.game.application;

import hu.bono.bigbank.dragons.game.domain.RestClientClientException;
import hu.bono.bigbank.dragons.game.domain.RestClientServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Component
@RequiredArgsConstructor
public class GameClient {

    static final String POST_URL = "https://dragonsofmugloar.com/api/v2/game/start";

    private final RestClient restClient;

    public PostGameResponse postGame() {
        try {
            return restClient.post()
                .uri(POST_URL)
                .retrieve()
                .body(PostGameResponse.class);
        } catch (RestClientResponseException exception) {
            if (exception.getStatusCode().is4xxClientError()) {
                throw new RestClientClientException(exception);
            } else {
                throw new RestClientServerException(exception);
            }
        }
    }
}
