package casino.banking.handler.user;

import org.springframework.http.HttpStatus;

public interface HttpStatusProvider {
    HttpStatus getStatus();
}
