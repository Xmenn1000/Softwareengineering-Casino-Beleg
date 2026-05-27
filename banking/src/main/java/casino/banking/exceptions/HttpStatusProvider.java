package casino.banking.exceptions;

import org.springframework.http.HttpStatus;

public interface HttpStatusProvider {
    HttpStatus getStatus();
}
