package hu.bono.bigbank.dragons.game.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.bono.bigbank.dragons.TestUtils;
import hu.bono.bigbank.dragons.common.application.ApiConfiguration;
import hu.bono.bigbank.dragons.common.domain.RestClientClientException;
import hu.bono.bigbank.dragons.common.domain.RestClientServerException;
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
    void testPostGameStartReturnsPostGameStartResponseWhenHttpStatusIs2Xx() throws Exception {
        final PostGameStartResponse expected = TestUtils.createPostGameStartResponse();
        final String postGameStartResponseString = objectMapper.writeValueAsString(expected);
        restServiceServer.expect(
                requestTo(apiConfiguration.getBaseUrl() + apiConfiguration.getEndpoints().getGameStart())
            )
            .andRespond(withSuccess(postGameStartResponseString, MediaType.APPLICATION_JSON));
        final PostGameStartResponse actual = underTest.postGameStart();
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testPostGameStartReturnsRestClientClientExceptionWhenHttpStatusIs4Xx() {
        restServiceServer.expect(
                requestTo(apiConfiguration.getBaseUrl() + apiConfiguration.getEndpoints().getGameStart())
            )
            .andRespond(withBadRequest());
        Assertions.assertThatThrownBy(underTest::postGameStart)
            .isInstanceOf(RestClientClientException.class);
    }

    @Test
    void testPostGameStartReturnsRestClientServerExceptionWhenHttpStatusIs5Xx() {
        restServiceServer.expect(
                requestTo(apiConfiguration.getBaseUrl() + apiConfiguration.getEndpoints().getGameStart())
            )
            .andRespond(withServerError());
        Assertions.assertThatThrownBy(underTest::postGameStart)
            .isInstanceOf(RestClientServerException.class);
    }
}
