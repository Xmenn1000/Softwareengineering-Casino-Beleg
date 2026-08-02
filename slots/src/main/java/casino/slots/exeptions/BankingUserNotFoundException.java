package casino.slots.exeptions;

import org.springframework.http.HttpStatus;

public class BankingUserNotFoundException extends HttpException {

    public BankingUserNotFoundException(Long userId) {
        super(HttpStatus.NOT_FOUND, "Banking user with id " + userId + " not found");
    }
}
