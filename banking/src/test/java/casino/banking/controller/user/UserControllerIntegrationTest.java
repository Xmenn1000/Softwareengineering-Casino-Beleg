package casino.banking.controller.user;

import casino.banking.services.user.UserService;
import casino.banking.view.user.response.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// good Source: https://www.baeldung.com/spring-boot-testing

@WebMvcTest(UserControllerIntegrationTest.class)
class UserControllerIntegrationTest {

    // ---------- Mocks ----------

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    // ---------- findById ----------
    @Test
    void findById_existingId_returns200WithUser() throws Exception {
        Long id = 1L;
        String firstName = "name";
        String lastName = "lastname";
        BigDecimal balance = new BigDecimal("100");
        when(userService.findById(1L)).thenReturn(new UserDTO(id, firstName, lastName, balance));

        mockMvc.perform(get("/casino/bank/api/user/{id}", id))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void findById_unknownId_returns404() {
    }

    // ---------- findAll ----------
    @Test
    void findAll_usersExist_returns200WithList() {
    }

    @Test
    void findAll_noUsers_returns200WithEmptyList() {
    }

    // ---------- create ----------
    @Test
    void create_validRequest_returns201WithUser() {
    }

    @Test
    void create_newUserStartsWithZeroBalance() {
    }

    @Test
    void create_blankFirstName_returns400() {
    }

    @Test
    void create_blankLastName_returns400() {
    }

    @Test
    void create_nullBody_returns400() {
    }

    // ---------- replaceById ----------
    @Test
    void replaceById_validRequest_returns200WithUpdatedUser() {
    }

    @Test
    void replaceById_unknownId_returns404() {
    }

    @Test
    void replaceById_invalidBody_returns400() {
    }

    // ---------- deleteById ----------
    @Test
    void deleteById_existingId_returns200WithoutId() {
    }

    @Test
    void deleteById_unknownId_returns404() {
    }

    // ---------- depositBalanceById ----------
    @Test
    void deposit_validAmount_returns200WithUpdatedBalance() {
    }

    @Test
    void deposit_minValues_zeroAmountZeroDecimals_returns200() {
    }

    @Test
    void deposit_maxDecimals_twoDecimals_returns200() {
    }

    @Test
    void deposit_largeAmount_returns200() {
    }

    @Test
    void deposit_unknownUser_returns404() {
    }

    @Test
    void deposit_negativeAmount_returns400() {
    }

    @Test
    void deposit_negativeDecimals_returns400() {
    }

    @Test
    void deposit_decimalsGreaterThanTwo_returns400() {
    }

    @Test
    void deposit_amountNotANumber_returns400() {
    }

    @Test
    void deposit_decimalsNotANumber_returns400() {
    }
}