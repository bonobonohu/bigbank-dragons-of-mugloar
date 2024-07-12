package hu.bono.bigbank.dragons.common.domain;

public class RestClientException extends RuntimeException {

    RestClientException(
        final Throwable cause
    ) {
        super(cause);
    }
}
