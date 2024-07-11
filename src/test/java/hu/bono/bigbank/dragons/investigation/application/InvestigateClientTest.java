package hu.bono.bigbank.dragons.investigation.application;

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

import static hu.bono.bigbank.dragons.investigation.application.InvestigateClient.prepareUri;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class InvestigateClientTest {

    private static final String GAME_ID = "GameId123";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();
    private final MockRestServiceServer restServiceServer = MockRestServiceServer.createServer(restTemplate);
    private final ApiConfiguration apiConfiguration = TestUtils.createApiConfiguration();
    private final InvestigateClient underTest = new InvestigateClient(RestClient.create(restTemplate), apiConfiguration);

    @Test
    void testPostInvestigateReputationReturnsPostInvestigateReputationResponseWhenHttpStatusIs2Xx() throws Exception {
        final PostInvestigateReputationResponse expected = TestUtils.createPostInvestigateReputationResponse();
        final String postInvestigateReputationResponseString = objectMapper.writeValueAsString(expected);
        restServiceServer.expect(
                requestTo(apiConfiguration.getBaseUrl()
                    + prepareUri(apiConfiguration.getEndpoints().getInvestigateReputation(), GAME_ID)
                )
            )
            .andRespond(withSuccess(postInvestigateReputationResponseString, MediaType.APPLICATION_JSON));
        final PostInvestigateReputationResponse actual = underTest.postInvestigateReputation(GAME_ID);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testPostInvestigateReputationReturnsRestClientClientExceptionWhenHttpStatusIs4Xx() {
        restServiceServer.expect(
                requestTo(apiConfiguration.getBaseUrl()
                    + prepareUri(apiConfiguration.getEndpoints().getInvestigateReputation(), GAME_ID)
                )
            )
            .andRespond(withBadRequest());
        Assertions.assertThatThrownBy(() -> underTest.postInvestigateReputation(GAME_ID))
            .isInstanceOf(RestClientClientException.class);
    }

    @Test
    void testPostInvestigateReputationReturnsRestClientServerExceptionWhenHttpStatusIs5Xx() {
        restServiceServer.expect(
                requestTo(apiConfiguration.getBaseUrl()
                    + prepareUri(apiConfiguration.getEndpoints().getInvestigateReputation(), GAME_ID)
                )
            )
            .andRespond(withServerError());
        Assertions.assertThatThrownBy(() -> underTest.postInvestigateReputation(GAME_ID))
            .isInstanceOf(RestClientServerException.class);
    }
}
