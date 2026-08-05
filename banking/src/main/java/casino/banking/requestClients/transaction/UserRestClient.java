package casino.banking.requestClients.transaction;

import casino.banking.exceptions.transaction.BadTransactionRequestException;
import casino.banking.view.user.UserDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigInteger;

@Log4j2
@Component
public class UserRestClient {

    private final RestClient restClient;

    public UserRestClient(@Qualifier("bankRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public UserDTO depositBalanceById(Long userId, BigInteger amount, int decimals) {
        return restClient.post()
                .uri("/user/{userId}/deposit/{amount}/{decimals}", userId, amount, decimals)
                .retrieve()
                .onStatus(status -> status.value() == 404, (request, reponse) -> {
                    throw new BadTransactionRequestException(String.format("No Valid User with id %d found", userId));
                })
                .onStatus(status -> status.value() == 400, (request, reponse) -> {
                    throw new BadTransactionRequestException(String.format("could not deposit %s.%d for %d", amount, decimals, userId));
                })
                .body(UserDTO.class);
    }

}
