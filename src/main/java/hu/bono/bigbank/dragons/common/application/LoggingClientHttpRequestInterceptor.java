package hu.bono.bigbank.dragons.common.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class LoggingClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingClientHttpRequestInterceptor.class);

    @Override
    public ClientHttpResponse intercept(
        final HttpRequest request,
        final byte[] body,
        final ClientHttpRequestExecution execution
    ) throws IOException {
        // LOG.info("Request: {} {}", request.getMethod(), request.getURI());
        // request.getHeaders().forEach((name, values) ->
        //     values.forEach(value ->
        //         LOG.info("Request Header: {} : {}", name, value)
        //     )
        // );
        final ClientHttpResponse response = execution.execute(request, body);
        // LOG.info("Response: {}", response.getStatusCode());
        // response.getHeaders().forEach((name, values) ->
        //     values.forEach(value ->
        //         // LOG.info("Response Header: {} : {}", name, value)
        //     )
        // );
        return response;
    }
}
