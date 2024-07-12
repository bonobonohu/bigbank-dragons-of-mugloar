package hu.bono.bigbank.dragons.common.domain;

public class LogWriterException extends RuntimeException {

    public LogWriterException(
        final Throwable cause
    ) {
        super(cause);
    }
}
