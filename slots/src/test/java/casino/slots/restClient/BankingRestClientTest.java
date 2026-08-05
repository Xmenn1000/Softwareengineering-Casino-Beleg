package casino.slots.restClient;

import casino.slots.exeptions.BadSlotsRequestException;
import casino.slots.exeptions.BankingUserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class BankingRestClientTest {

    private MockRestServiceServer mockServer;
    private BankingRestClient bankingRestClient;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder()
                .baseUrl("http://banking-test/casino/bank/api");

        mockServer = MockRestServiceServer
                .bindTo(builder)
                .build();

        bankingRestClient =
                new BankingRestClient(builder.build());

        /*
         * Das Feld wird im echten Programm über @Value gesetzt.
         * Im Unit-Test starten wir keinen Spring-Kontext,
         * deshalb setzen wir es hier manuell.
         */
        ReflectionTestUtils.setField(
                bankingRestClient,
                "invoicingParty",
                "SLOTS"
        );
    }

    @Test
    void shouldFindUserById() {
        mockServer.expect(
                        once(),
                        requestTo(
                                "http://banking-test/casino/bank/api/user/7"
                        )
                )
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(
                        """
                        {
                          "id": 7,
                          "firstName": "Max",
                          "lastName": "Mustermann",
                          "balance": 125.50
                        }
                        """,
                        MediaType.APPLICATION_JSON
                ));

        BankingUserDTO user =
                bankingRestClient.findUserById(7L);

        assertNotNull(user);
        assertEquals(7L, user.getId());
        assertEquals("Max", user.getFirstName());
        assertEquals("Mustermann", user.getLastName());

        assertBigDecimalEquals(
                "125.50",
                user.getBalance()
        );

        mockServer.verify();
    }

    @Test
    void shouldThrowWhenUserDoesNotExist() {
        mockServer.expect(
                        requestTo(
                                "http://banking-test/casino/bank/api/user/99"
                        )
                )
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        BankingUserNotFoundException exception =
                assertThrows(
                        BankingUserNotFoundException.class,
                        () -> bankingRestClient.findUserById(99L)
                );

        assertEquals(
                "Banking user with id 99 not found",
                exception.getMessage()
        );

        mockServer.verify();
    }

    @Test
    void shouldCreateSlotsTransaction() {
        mockServer.expect(
                        requestTo(
                                "http://banking-test/casino/bank/api/transactions/user/7"
                        )
                )
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(
                        MediaType.APPLICATION_JSON
                ))
                .andExpect(content().json(
                        """
                        {
                          "gameService": "SLOTS",
                          "amount": 12.50
                        }
                        """
                ))
                .andRespond(withSuccess());

        assertDoesNotThrow(() ->
                bankingRestClient.createSlotsTransaction(
                        7L,
                        new BigDecimal("12.50")
                )
        );

        mockServer.verify();
    }

    @Test
    void shouldThrowWhenTransactionUserDoesNotExist() {
        mockServer.expect(
                        requestTo(
                                "http://banking-test/casino/bank/api/transactions/user/99"
                        )
                )
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertThrows(
                BankingUserNotFoundException.class,
                () -> bankingRestClient.createSlotsTransaction(
                        99L,
                        new BigDecimal("10.00")
                )
        );

        mockServer.verify();
    }

    @Test
    void shouldThrowWhenBankingRejectsTransaction() {
        mockServer.expect(
                        requestTo(
                                "http://banking-test/casino/bank/api/transactions/user/7"
                        )
                )
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        assertThrows(
                BadSlotsRequestException.class,
                () -> bankingRestClient.createSlotsTransaction(
                        7L,
                        new BigDecimal("-10.00")
                )
        );

        mockServer.verify();
    }

    private void assertBigDecimalEquals(
            String expected,
            BigDecimal actual
    ) {
        assertEquals(
                0,
                new BigDecimal(expected).compareTo(actual)
        );
    }
}