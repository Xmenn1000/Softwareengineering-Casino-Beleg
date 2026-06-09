package casino.banking.exceptions;

import org.springframework.http.HttpStatus;

public abstract class HttpException extends RuntimeException implements HttpStatusProvider {

    private final HttpStatus httpStatus;

    public HttpStatus getHTTPStatus() {
        return httpStatus;
    }

    protected HttpException(HttpStatus status, String message) {
        super(message);
        this.httpStatus = status;
    }

    protected HttpException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = status;
    }
}
