package casino.roulette.requestClients.banking;

import casino.roulette.config.RouletteProperties;
import casino.roulette.exceptions.BadRouletteRequestException;
import casino.roulette.exceptions.BankingUserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withResourceNotFound;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class BankingRestClientTest {

    private MockRestServiceServer server;
    private BankingRestClient bankingRestClient;

    @BeforeEach
    void setUp() {
        RestClient.Builder restClientBuilder = RestClient.builder()
                .baseUrl("http://banking-service");
        server = MockRestServiceServer.bindTo(restClientBuilder).build();

        RouletteProperties rouletteProperties = new RouletteProperties();
        rouletteProperties.getBanking().setInvoicingParty("ROULETTE");

        bankingRestClient = new BankingRestClient(
                restClientBuilder.build(),
                rouletteProperties
        );
    }

    @Test
    void findUserByIdReturnsMappedUser() {
        server.expect(requestTo("http://banking-service/user/1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                        {
                          "id": 1,
                          "firstName": "Ada",
                          "lastName": "Lovelace",
                          "balance": 100.00
                        }
                        """, MediaType.APPLICATION_JSON));

        BankingUserDTO result = bankingRestClient.findUserById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Ada", result.getFirstName());
        assertEquals("Lovelace", result.getLastName());
        assertEquals(0, new BigDecimal("100.00").compareTo(result.getBalance()));
        server.verify();
    }

    @Test
    void findUserByIdThrowsWhenBankingReturnsNotFound() {
        server.expect(requestTo("http://banking-service/user/99"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withResourceNotFound());

        assertThrows(BankingUserNotFoundException.class, () -> bankingRestClient.findUserById(99L));
        server.verify();
    }

    @Test
    void createRouletteTransactionSendsExpectedRequestBody() {
        server.expect(requestTo("http://banking-service/transactions/user/1"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json("""
                        {
                          "gameService": "ROULETTE",
                          "amount": -10.00
                        }
                        """))
                .andRespond(withSuccess());

        bankingRestClient.createRouletteTransaction(1L, new BigDecimal("-10.00"));

        server.verify();
    }

    @Test
    void createRouletteTransactionThrowsWhenUserDoesNotExist() {
        server.expect(requestTo("http://banking-service/transactions/user/99"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withResourceNotFound());

        assertThrows(
                BankingUserNotFoundException.class,
                () -> bankingRestClient.createRouletteTransaction(99L, new BigDecimal("10.00"))
        );
        server.verify();
    }

    @Test
    void createRouletteTransactionThrowsWhenBankingRejectsTransaction() {
        server.expect(requestTo("http://banking-service/transactions/user/1"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withBadRequest());

        assertThrows(
                BadRouletteRequestException.class,
                () -> bankingRestClient.createRouletteTransaction(1L, new BigDecimal("10.00"))
        );
        server.verify();
    }
}
