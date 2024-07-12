package hu.bono.bigbank.dragons.common.domain;

public class RestClientServerException extends RestClientException {

    public RestClientServerException(
        final Throwable cause
    ) {
        super(cause);
    }
}
