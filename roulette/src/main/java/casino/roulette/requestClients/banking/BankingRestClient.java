package casino.roulette.requestClients.banking;

import casino.roulette.exceptions.BadRouletteRequestException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Component
public class BankingRestClient {

    private final RestClient restClient;

    public BankingRestClient(@Qualifier("bankRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public BankingUserDTO findUserById(Long userId) {
        return restClient.get()
                .uri("/user/{id}", userId)
                .retrieve()
                .onStatus(status -> status.value() == 404, (request, response) -> {
                    throw new BadRouletteRequestException("User with id " + userId + " not found");
                })
                .body(BankingUserDTO.class);
    }

    public void createRouletteTransaction(Long userId, BigDecimal amount) {
        BankingTransactionRequestDTO body = new BankingTransactionRequestDTO("ROULETTE", amount);

        restClient.post()
                .uri("/transactions/user/{userId}", userId)
                .body(body)
                .retrieve()
                .onStatus(status -> status.value() == 404, (request, response) -> {
                    throw new BadRouletteRequestException("User with id " + userId + " not found");
                })
                .onStatus(status -> status.value() == 400, (request, response) -> {
                    throw new BadRouletteRequestException("Banking rejected roulette transaction");
                })
                .toBodilessEntity();
    }
}
