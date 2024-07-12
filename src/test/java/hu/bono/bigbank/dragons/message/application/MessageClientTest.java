package hu.bono.bigbank.dragons.message.application;

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

import java.util.List;

import static hu.bono.bigbank.dragons.message.application.MessageClient.prepareUri;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class MessageClientTest {

    private static final String GAME_ID = "GameId123";
    private static final String AD_ID = "AdId123";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();
    private final MockRestServiceServer restServiceServer = MockRestServiceServer.createServer(restTemplate);
    private final ApiConfiguration apiConfiguration = TestUtils.createApiConfiguration();
    private final MessageClient underTest = new MessageClient(RestClient.create(restTemplate), apiConfiguration);

    @Test
    void testGetMessagesReturnsGetMessagesResponseItemsWhenHttpStatusIs2Xx() {
        final List<GetMessagesResponseItem> expected = TestUtils.createGetMessagesResponseItems();
        final String getMessagesResponseItemsString = """
                [
                    {
                        "adId":"AdId123",
                        "message":"Help Tarquinius Reynell to fix their bucket",
                        "reward":100,
                        "expiresIn":10,
                        "encrypted":null,
                        "probability":"Piece of cake"
                    },
                    {
                        "adId":"dWFMamdrcmk=",
                        "message":"SW5maWx0cmF0ZSBUaGUgRWFnbGUgU29sZGllcnMgYW5kIHJlY292ZXIgdGhlaXIgc2VjcmV0cy4=",
                        "reward":100,
                        "expiresIn":10,
                        "encrypted":1,
                        "probability":"UGxheWluZyB3aXRoIGZpcmU="
                    }
                ]
            """.trim();
        restServiceServer.expect(
                requestTo(
                    apiConfiguration.getBaseUrl()
                        + prepareUri(apiConfiguration.getEndpoints().getMessages(), GAME_ID)
                )
            )
            .andRespond(withSuccess(getMessagesResponseItemsString, MediaType.APPLICATION_JSON));
        final List<GetMessagesResponseItem> actual = underTest.getMessages(GAME_ID);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testGetMessagesReturnsRestClientClientExceptionWhenHttpStatusIs4Xx() {
        restServiceServer.expect(
                requestTo(
                    apiConfiguration.getBaseUrl()
                        + prepareUri(apiConfiguration.getEndpoints().getMessages(), GAME_ID)
                )
            )
            .andRespond(withBadRequest());
        Assertions.assertThatThrownBy(() -> underTest.getMessages(GAME_ID))
            .isInstanceOf(RestClientClientException.class);
    }

    @Test
    void testGetMessagesReturnsRestClientServerExceptionWhenHttpStatusIs5Xx() {
        restServiceServer.expect(
                requestTo(
                    apiConfiguration.getBaseUrl()
                        + prepareUri(apiConfiguration.getEndpoints().getMessages(), GAME_ID)
                )
            )
            .andRespond(withServerError());
        Assertions.assertThatThrownBy(() -> underTest.getMessages(GAME_ID))
            .isInstanceOf(RestClientServerException.class);
    }

    @Test
    void testPostSolveAdReturnsPostSolveAdResponseWhenHttpStatusIs2Xx() throws Exception {
        final PostSolveAdResponse expected = TestUtils.createPostSolveAdResponse();
        final String postSolveAdResponseString = objectMapper.writeValueAsString(expected);
        restServiceServer.expect(
                requestTo(
                    apiConfiguration.getBaseUrl()
                        + MessageClient.prepareUri(apiConfiguration.getEndpoints().getSolveAd(), GAME_ID, AD_ID)
                )
            )
            .andRespond(withSuccess(postSolveAdResponseString, MediaType.APPLICATION_JSON));
        final PostSolveAdResponse actual = underTest.postSolveAd(GAME_ID, AD_ID);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testPostSolveAdReturnsRestClientClientExceptionWhenHttpStatusIs4Xx() {
        restServiceServer.expect(
                requestTo(
                    apiConfiguration.getBaseUrl()
                        + MessageClient.prepareUri(apiConfiguration.getEndpoints().getSolveAd(), GAME_ID, AD_ID)
                )
            )
            .andRespond(withBadRequest());
        Assertions.assertThatThrownBy(() -> underTest.postSolveAd(GAME_ID, AD_ID))
            .isInstanceOf(RestClientClientException.class);
    }

    @Test
    void testPostSolveAdReturnsRestClientServerExceptionWhenHttpStatusIs5Xx() {
        restServiceServer.expect(
                requestTo(
                    apiConfiguration.getBaseUrl()
                        + MessageClient.prepareUri(apiConfiguration.getEndpoints().getSolveAd(), GAME_ID, AD_ID)
                )
            )
            .andRespond(withServerError());
        Assertions.assertThatThrownBy(() -> underTest.postSolveAd(GAME_ID, AD_ID))
            .isInstanceOf(RestClientServerException.class);
    }
}
