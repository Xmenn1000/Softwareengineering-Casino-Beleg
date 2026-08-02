package casino.slots.exeptions;

import org.springframework.http.HttpStatus;

public class BadSlotsRequestException extends HttpException {

    public BadSlotsRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
