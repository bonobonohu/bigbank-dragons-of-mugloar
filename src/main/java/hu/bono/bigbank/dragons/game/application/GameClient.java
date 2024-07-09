package hu.bono.bigbank.dragons.game.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class GameClient {

    static final String POST_URL = "https://dragonsofmugloar.com/api/v2/game/start";

    private final RestClient restClient;

    public PostGameResponse postGame() {
        return restClient.post()
            .uri(POST_URL)
            .retrieve()
            .body(PostGameResponse.class);
    }
}
