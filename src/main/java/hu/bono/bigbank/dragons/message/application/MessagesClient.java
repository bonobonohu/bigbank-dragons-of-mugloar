package hu.bono.bigbank.dragons.message.application;

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
public class MessagesClient {

    private final RestClient restClient;
    private final ApiConfiguration apiConfiguration;

    static String prepareUri(String endpointTemplate, String gameId) {
        return endpointTemplate.replace("{gameId}", gameId);
    }

    public List<GetMessagesResponseItem> getMessages(String gameId) {
        try {
            return restClient.get()
                .uri(
                    apiConfiguration.getBaseUrl() +
                        prepareUri(apiConfiguration.getEndpoints().getMessages(), gameId)
                )
                .retrieve()
                .body(
                    new ParameterizedTypeReference<>() {
                    })
                ;
        } catch (RestClientResponseException exception) {
            if (exception.getStatusCode().is4xxClientError()) {
                throw new RestClientClientException(exception);
            } else {
                throw new RestClientServerException(exception);
            }
        }
    }
}
