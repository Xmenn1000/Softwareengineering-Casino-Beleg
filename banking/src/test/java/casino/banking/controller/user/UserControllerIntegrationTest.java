package casino.banking.controller.user;

import casino.banking.handler.user.UserNotFoundExeption;
import casino.banking.services.user.UserService;
import casino.banking.view.user.request.UserRequestDTO;
import casino.banking.view.user.response.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Good Sources:
 * https://www.baeldung.com/spring-boot-testing
 * https://www.geeksforgeeks.org/springboot/spring-boot-mockmvc-example/
 * https://springboot-123.mizucoffee.com/en/blog/spring-boot-mockmvc-controller-test-guide/
 */

@WebMvcTest(UserController.class)
class UserControllerIntegrationTest {

    // ---------- Mocks ----------

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

        verify(userService, times(1)).findById(1L);
    }

    @Test
    void findById_unknownId_returns404() throws Exception {

        Long id = 1L;
        when(userService.findById(id)).thenThrow(new UserNotFoundExeption(id));

        mockMvc.perform(get("/casino/bank/api/user/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(userService, times(1)).findById(1L);
    }

    // ---------- findAll ----------
    @Test
    void findAll_usersExist_returns200WithList() throws Exception {
        UserDTO user1 = new UserDTO(1L, "a", "b", new BigDecimal(1));
        UserDTO user2 = new UserDTO(2L, "c", "d", new BigDecimal(2));
        List<UserDTO> userList = List.of(user1, user2);
        when(userService.findAll()).thenReturn(userList);

        mockMvc.perform(get("/casino/bank/api/users"))
                .andDo(print())
                .andExpect(status().isOk());
                //TODO: Liste abgleichen

        verify(userService, times(1)).findAll();

    }

    @Test
    void findAll_noUsers_returns200WithEmptyList() throws Exception {
        List<UserDTO> userList = List.of();
        when(userService.findAll()).thenReturn(userList);

        mockMvc.perform(get("/casino/bank/api/users"))
                .andDo(print())
                .andExpect(status().isOk());
                //TODO: check List empty

        verify(userService, times(1)).findAll();
    }

    // ---------- create ----------
    @Test
    void create_validRequest_returns201WithUser() throws Exception {
        when(userService.create(any())).thenReturn(new UserDTO(1L, "firstName", "lastName", BigDecimal.ZERO));

        mockMvc.perform(post("/casino/bank/api/user"))
                .andDo(print())
                .andExpect(status().isCreated());
                //TODO: checks that its the user

        verify(userService, times(1)).create(any());
    }

    @Test
    void create_blankFirstName_returns400() throws Exception {
        UserRequestDTO userRequest = new UserRequestDTO("firstName", "lastName");
        when(userService.create(any())).thenReturn(new UserDTO(1L, "", "lastName", BigDecimal.ZERO));

        mockMvc.perform(post("/casino/bank/api/user"))
                .andDo(print())
                .andExpect(status().isBadRequest());
        //TODO: imrpove

        verify(userService, times(1)).create(any());
    }

    @Test
    void create_blankLastName_returns400() throws Exception {
        when(userService.create(any())).thenReturn(new UserDTO(1L, "firstName", "", BigDecimal.ZERO));

        mockMvc.perform(post("/casino/bank/api/user"))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(userService, times(1)).create(any());
    }

    @Test
    void create_nullBody_returns400() throws Exception {

        mockMvc.perform(post("/casino/bank/api/user"))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(userService, times(1)).create(any());
    }

    // ---------- replaceById ----------
    @Test
    void replaceById_validRequest_returns200WithUpdatedUser() throws Exception {
        fail();
    }

    @Test
    void replaceById_unknownId_returns404() throws Exception {
        fail();
    }

    @Test
    void replaceById_invalidBody_returns400() throws Exception {
        fail();
    }

    // ---------- deleteById ----------
    @Test
    void deleteById_existingId_returns200WithoutId() throws Exception {
        fail();
    }

    @Test
    void deleteById_unknownId_returns404() throws Exception {
        fail();
    }

    // ---------- depositBalanceById ----------
    @Test
    void deposit_validAmount_returns200WithUpdatedBalance() throws Exception {
        Long id = 1L;
//        when(userService.depositBalanceById(any(), any(), any())).thenReturn()

        mockMvc.perform(post("/user/{userId}/deposit/{amount}/{decimals}", 1L, new BigDecimal(-20), 20))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(userService, times(1)).findById(id);
    }

    @Test
    void deposit_minValues_zeroAmountZeroDecimals_returns200() throws Exception {
        mockMvc.perform(post("/user/{userId}/deposit/{amount}/{decimals}", 1L, new BigDecimal(0), 0))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deposit_maxDecimals_twoDecimals_returns200() throws Exception {
        mockMvc.perform(post("/user/{userId}/deposit/{amount}/{decimals}", 1L, new BigDecimal(20), 99))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deposit_largeAmount_returns200() throws Exception {
        fail();
    }

    @Test
    void deposit_unknownUser_returns404() throws Exception {
        Long id = 1L;
        when(userService.findById(id)).thenThrow(new UserNotFoundExeption(id));

        mockMvc.perform(post("/casino/bank/api/user/{userId}/deposit/{amount}/{decimals}", 1L, new BigDecimal(20), 20))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(userService, times(1)).findById(id);
    }

    @Test
    void deposit_negativeAmount_returns400() throws Exception {
        mockMvc.perform(post("/casino/bank/api/user/{userId}/deposit/{amount}/{decimals}", 1L, new BigDecimal(-20), 20))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void deposit_negativeDecimals_returns400() throws Exception {
        mockMvc.perform(post("/casino/bank/api/user/{userId}/deposit/{amount}/{decimals}", 1L, new BigDecimal(20), -20))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void deposit_decimalsGreaterThanTwo_returns400() throws Exception {
        mockMvc.perform(post("/casino/bank/api/user/{userId}/deposit/{amount}/{decimals}", 1L, new BigDecimal(20), 200))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void deposit_amountNotANumber_returns400() throws Exception {
        mockMvc.perform(post("/casino/bank/api/user/{userId}/deposit/{amount}/{decimals}", 1L, "test", 2))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void deposit_decimalsNotANumber_returns400() throws Exception {
        mockMvc.perform(post("/user/{userId}/deposit/{amount}/{decimals}", 1L, new BigDecimal(20), "test"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}