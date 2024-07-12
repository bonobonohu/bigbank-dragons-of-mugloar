package hu.bono.bigbank.dragons.shop.application;

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
public class ShopClient {

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
        final String itemId
    ) {
        return endpointTemplate
            .replace("{gameId}", gameId)
            .replace("{itemId}", itemId);
    }

    public List<GetShopResponseItem> getShop(
        final String gameId
    ) {
        try {
            return restClient.get()
                .uri(
                    apiConfiguration.getBaseUrl()
                        + prepareUri(apiConfiguration.getEndpoints().getShop(), gameId)
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

    public PostShopBuyItemResponse postShopBuyItem(
        final String gameId,
        final String itemId
    ) {
        try {
            return restClient.post()
                .uri(
                    apiConfiguration.getBaseUrl()
                        + prepareUri(apiConfiguration.getEndpoints().getShopBuyItem(), gameId, itemId)
                )
                .retrieve()
                .body(PostShopBuyItemResponse.class);
        } catch (RestClientResponseException exception) {
            if (exception.getStatusCode().is4xxClientError()) {
                throw new RestClientClientException(exception);
            } else {
                throw new RestClientServerException(exception);
            }
        }
    }
}
