package casino.slots.restClient;

import casino.slots.exeptions.BadSlotsRequestException;
import casino.slots.exeptions.BankingUserNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Component
public class BankingRestClient {

    private final RestClient restClient;

    @Value("${slots.invoicingParty}")
    private String invoicingParty;

    public BankingRestClient(
            @Qualifier("bankRestClient") RestClient restClient) {
        this.restClient = restClient;
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

    public void createSlotsTransaction(Long userId, BigDecimal amount) {
        BankingTransactionRequestDTO body = new BankingTransactionRequestDTO(
                invoicingParty,
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
                    throw new BadSlotsRequestException("Banking rejected roulette transaction");
                })
                .toBodilessEntity();
    }
}
