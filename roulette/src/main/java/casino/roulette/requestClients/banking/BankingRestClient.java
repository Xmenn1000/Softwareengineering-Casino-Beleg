package casino.roulette.requestClients.banking;

import casino.roulette.config.RouletteProperties;
import casino.roulette.exceptions.BadRouletteRequestException;
import casino.roulette.exceptions.BankingUserNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Component
public class BankingRestClient {

    private final RestClient restClient;
    private final RouletteProperties rouletteProperties;

    public BankingRestClient(
            @Qualifier("bankRestClient") RestClient restClient,
            RouletteProperties rouletteProperties) {
        this.restClient = restClient;
        this.rouletteProperties = rouletteProperties;
    }

    public BankingUserDTO findUserById(Long userId) {
        return restClient.get()
                .uri("/user/{id}", userId)
                .retrieve()
                .onStatus(status -> status.value() == 404, (request, response) -> {
                    throw new BankingUserNotFoundException(userId);
                })
                .body(BankingUserDTO.class);
    }

    public void createRouletteTransaction(Long userId, BigDecimal amount) {
        BankingTransactionRequestDTO body = new BankingTransactionRequestDTO(
                rouletteProperties.getBanking().getInvoicingParty(),
                amount
        );

        restClient.post()
                .uri("/transactions/user/{userId}", userId)
                .body(body)
                .retrieve()
                .onStatus(status -> status.value() == 404, (request, response) -> {
                    throw new BankingUserNotFoundException(userId);
                })
                .onStatus(status -> status.value() == 400, (request, response) -> {
                    throw new BadRouletteRequestException("Banking rejected roulette transaction");
                })
                .toBodilessEntity();
    }
}
