package hu.bono.bigbank.dragons.game.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.bono.bigbank.dragons.TestUtils;
import hu.bono.bigbank.dragons.common.application.ApiConfiguration;
import hu.bono.bigbank.dragons.game.domain.RestClientClientException;
import hu.bono.bigbank.dragons.game.domain.RestClientServerException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class GameClientTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();
    private final MockRestServiceServer restServiceServer = MockRestServiceServer.createServer(restTemplate);
    private final ApiConfiguration apiConfiguration = TestUtils.createApiConfiguration();
    private final GameClient underTest = new GameClient(RestClient.create(restTemplate), apiConfiguration);

    @Test
    void testPostGameReturnsPostGameResponseWhenHttpStatusIs2xx() throws Exception {
        final PostGameResponse expected = TestUtils.createPostGameResponse();
        final String postGameResponseString = objectMapper.writeValueAsString(expected);
        restServiceServer.expect(
                requestTo(apiConfiguration.getBaseUrl() + apiConfiguration.getEndpoints().getGameStart())
            )
            .andRespond(withSuccess(postGameResponseString, MediaType.APPLICATION_JSON));
        final PostGameResponse actual = underTest.postGame();
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testPostGameReturnsRestClientClientExceptionWhenHttpStatusIs4xx() {
        restServiceServer.expect(
                requestTo(apiConfiguration.getBaseUrl() + apiConfiguration.getEndpoints().getGameStart())
            )
            .andRespond(withBadRequest());
        Assertions.assertThatThrownBy(underTest::postGame)
            .isInstanceOf(RestClientClientException.class);
    }

    @Test
    void testPostGameReturnsRestClientServerExceptionWhenHttpStatusIs5xx() {
        restServiceServer.expect(
                requestTo(apiConfiguration.getBaseUrl() + apiConfiguration.getEndpoints().getGameStart())
            )
            .andRespond(withServerError());
        Assertions.assertThatThrownBy(underTest::postGame)
            .isInstanceOf(RestClientServerException.class);
    }
}
