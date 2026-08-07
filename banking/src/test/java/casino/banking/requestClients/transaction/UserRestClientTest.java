package casino.banking.requestClients.transaction;

import casino.banking.exceptions.transaction.BadTransactionRequestException;
import casino.banking.view.user.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;
;

//https://www.javathinking.com/blog/mocking-a-rest-call-with-mockrestserviceserver/
class UserRestClientTest {

    private UserRestClient userRestClient;
    private MockRestServiceServer mockServer;
    private String baseUrl = "http://banking-test/casino/bank/api";


    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder()
                .baseUrl(baseUrl);
        mockServer = MockRestServiceServer.bindTo(builder).build();
        userRestClient = new UserRestClient(builder.build());
    }

    // ---------- depositBalanceById ----------
    @Test
    void depositBalanceById_success_returnsUpdatedUser() {
        Long userId = 1L;
        BigInteger amount = new BigInteger("20");
        int decimals = 33;
        String firstName = "firstName";
        String lastName = "lastName";
        BigDecimal balance = new BigDecimal("100.00");

        String body = """
        {
          "id": %d,
          "firstName": "%s",
          "lastName": "%s",
          "balance": %s
        }
        """.formatted(1, "firstName", "lastName", balance);

        mockServer.expect(requestTo(baseUrl + String.format("/user/%d/deposit/%d/%d", userId, amount, decimals)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(body, MediaType.APPLICATION_JSON));


        UserDTO user = userRestClient.depositBalanceById(1L, amount, decimals);
        assertEquals(userId, user.getId());
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(balance, user.getBalance());
    }

    @Test
    void depositBalanceById_userNotFound_throwsBadTransactionRequest() {
        Long userId = 1L;
        BigInteger amount = new BigInteger("20");
        int decimals = 33;

        mockServer.expect(requestTo(baseUrl + String.format("/user/%d/deposit/%d/%d", userId, amount, decimals)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withResourceNotFound());


        assertThrows(BadTransactionRequestException.class, () -> userRestClient.depositBalanceById(1L, amount, decimals));
    }

    @Test
    void depositBalanceById_badRequest_throwsBadTransactionRequest() {
        Long userId = 1L;
        BigInteger amount = new BigInteger("20");
        int decimals = 33;

        mockServer.expect(requestTo(baseUrl + String.format("/user/%d/deposit/%d/%d", userId, amount, decimals)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withBadRequest());


        assertThrows(BadTransactionRequestException.class, () -> userRestClient.depositBalanceById(1L, amount, decimals));
    }

    // ---------- withDrawById ----------
    @Test
    void withDrawById_success_returnsUpdatedUser() {
        Long userId = 1L;
        BigInteger amount = new BigInteger("20");
        int decimals = 33;
        String firstName = "firstName";
        String lastName = "lastName";
        BigDecimal balance = new BigDecimal("100.00");

        String body = """
        {
          "id": %d,
          "firstName": "%s",
          "lastName": "%s",
          "balance": %s
        }
        """.formatted(1, "firstName", "lastName", balance);

        mockServer.expect(requestTo(baseUrl + String.format("/user/%d/withDraw/%d/%d", userId, amount, decimals)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(body, MediaType.APPLICATION_JSON));


        UserDTO user = userRestClient.withDrawById(1L, amount, decimals);
        assertEquals(userId, user.getId());
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(balance, user.getBalance());
    }

    @Test
    void withDrawById_userNotFound_throwsBadTransactionRequest() {
        Long userId = 1L;
        BigInteger amount = new BigInteger("20");
        int decimals = 33;

        mockServer.expect(requestTo(baseUrl + String.format("/user/%d/withDraw/%d/%d", userId, amount, decimals)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withResourceNotFound());


        assertThrows(BadTransactionRequestException.class, () -> userRestClient.withDrawById(1L, amount, decimals));
    }

    @Test
    void withDrawById_badRequest_throwsBadTransactionRequest() {
        Long userId = 1L;
        BigInteger amount = new BigInteger("20");
        int decimals = 33;

        mockServer.expect(requestTo(baseUrl + String.format("/user/%d/withDraw/%d/%d", userId, amount, decimals)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withBadRequest());


        assertThrows(BadTransactionRequestException.class, () -> userRestClient.withDrawById(1L, amount, decimals));
    }
}
