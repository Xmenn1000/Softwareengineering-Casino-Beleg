package casino.slots.exeptions;

import org.springframework.http.HttpStatus;

public interface HttpStatusProvider {
    HttpStatus getHTTPStatus();
}
