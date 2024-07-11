package hu.bono.bigbank.dragons.shop.application;

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

import static hu.bono.bigbank.dragons.shop.application.ShopClient.prepareUri;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class ShopClientTest {

    private static final String GAME_ID = "GameId123";
    private static final String ITEM_ID = "hpot";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();
    private final MockRestServiceServer restServiceServer = MockRestServiceServer.createServer(restTemplate);
    private final ApiConfiguration apiConfiguration = TestUtils.createApiConfiguration();
    private final ShopClient underTest = new ShopClient(RestClient.create(restTemplate), apiConfiguration);

    @Test
    void testGetShopReturnsGetShopResponseItemsWhenHttpStatusIs2Xx() throws Exception {
        final List<GetShopResponseItem> expected = TestUtils.createGetShopResponseItems();
        final String getShopResponseItemsString = objectMapper.writeValueAsString(expected);
        restServiceServer.expect(
                requestTo(apiConfiguration.getBaseUrl()
                    + prepareUri(apiConfiguration.getEndpoints().getShop(), GAME_ID)
                )
            )
            .andRespond(withSuccess(getShopResponseItemsString, MediaType.APPLICATION_JSON));
        final List<GetShopResponseItem> actual = underTest.getShop(GAME_ID);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testGetShopReturnsRestClientClientExceptionWhenHttpStatusIs4Xx() {
        restServiceServer.expect(
                requestTo(apiConfiguration.getBaseUrl()
                    + prepareUri(apiConfiguration.getEndpoints().getShop(), GAME_ID)
                )
            )
            .andRespond(withBadRequest());
        Assertions.assertThatThrownBy(() -> underTest.getShop(GAME_ID))
            .isInstanceOf(RestClientClientException.class);
    }

    @Test
    void testGetShopReturnsRestClientServerExceptionWhenHttpStatusIs5Xx() {
        restServiceServer.expect(
                requestTo(apiConfiguration.getBaseUrl()
                    + prepareUri(apiConfiguration.getEndpoints().getShop(), GAME_ID)
                )
            )
            .andRespond(withServerError());
        Assertions.assertThatThrownBy(() -> underTest.getShop(GAME_ID))
            .isInstanceOf(RestClientServerException.class);
    }

    @Test
    void testPostShopBuyItemReturnsPostShopBuyItemResponseWhenHttpStatusIs2Xx() throws Exception {
        final PostShopBuyItemResponse expected = TestUtils.createPostShopBuyItemResponse();
        final String postShopBuyItemResponseString = objectMapper.writeValueAsString(expected);
        restServiceServer.expect(
                requestTo(apiConfiguration.getBaseUrl()
                    + prepareUri(apiConfiguration.getEndpoints().getShopBuyItem(), GAME_ID, ITEM_ID)
                )
            )
            .andRespond(withSuccess(postShopBuyItemResponseString, MediaType.APPLICATION_JSON));
        final PostShopBuyItemResponse actual = underTest.postShopBuyItem(GAME_ID, ITEM_ID);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testPostShopBuyItemReturnsRestClientClientExceptionWhenHttpStatusIs4Xx() {
        restServiceServer.expect(
                requestTo(apiConfiguration.getBaseUrl()
                    + prepareUri(apiConfiguration.getEndpoints().getShopBuyItem(), GAME_ID, ITEM_ID)
                )
            )
            .andRespond(withBadRequest());
        Assertions.assertThatThrownBy(() -> underTest.postShopBuyItem(GAME_ID, ITEM_ID))
            .isInstanceOf(RestClientClientException.class);
    }

    @Test
    void testPostShopBuyItemReturnsRestClientServerExceptionWhenHttpStatusIs5Xx() {
        restServiceServer.expect(
                requestTo(apiConfiguration.getBaseUrl()
                    + prepareUri(apiConfiguration.getEndpoints().getShopBuyItem(), GAME_ID, ITEM_ID)
                )
            )
            .andRespond(withServerError());
        Assertions.assertThatThrownBy(() -> underTest.postShopBuyItem(GAME_ID, ITEM_ID))
            .isInstanceOf(RestClientServerException.class);
    }
}
