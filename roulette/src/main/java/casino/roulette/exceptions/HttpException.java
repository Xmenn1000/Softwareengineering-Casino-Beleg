package casino.roulette.exceptions;

import org.springframework.http.HttpStatus;

public abstract class HttpException extends RuntimeException {

    private final HttpStatus httpStatus;

    protected HttpException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
