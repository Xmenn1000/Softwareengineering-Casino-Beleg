package casino.slots.exeptions;

import org.springframework.http.HttpStatus;

public class SlotsUserNotFoundException extends HttpException {

    public SlotsUserNotFoundException(Long userId) {
        super(HttpStatus.NOT_FOUND, "User with id " + userId + " not found");
    }
}
