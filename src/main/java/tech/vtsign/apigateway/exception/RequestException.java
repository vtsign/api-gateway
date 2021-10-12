package tech.vtsign.apigateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RequestException extends ResponseStatusException {
    private final HttpStatus status;
    private final String reason;

    public RequestException(HttpStatus status, String reason) {
        super(status, reason);
        this.status = status;
        this.reason = reason;
    }

    @Override
    public String getMessage() {
        return reason;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }
}
