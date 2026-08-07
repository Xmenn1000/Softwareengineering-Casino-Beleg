package casino.banking.controller.user;

import casino.banking.exceptions.user.UserModelValidityBreachException;
import casino.banking.exceptions.user.UserNotFoundException;
import casino.banking.request.user.UserRequestDTO;
import casino.banking.services.user.UserService;
import casino.banking.view.user.UserDTO;
import casino.banking.view.user.UserDeleteDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    // ---------- findById ----------
    @Test
    void findById_existingId_returnsUserBody() {
        Long id = 1L;
        UserDTO dto = new UserDTO(id, "name", "lastname", new BigDecimal("100"));
        when(userService.findById(id)).thenReturn(dto);

        ResponseEntity<UserDTO> response = userController.findById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
        verify(userService, times(1)).findById(id);
    }

    @Test
    void findById_unknownId_propagatesException() {
        Long id = 1L;
        when(userService.findById(id)).thenThrow(new UserNotFoundException(id));

        assertThrows(UserNotFoundException.class, () -> userController.findById(id));
        verify(userService, times(1)).findById(id);
    }

    // ---------- findAll ----------
    @Test
    void findAll_usersExist_returnsListBody() {
        UserDTO user1 = new UserDTO(1L, "a", "b", new BigDecimal(1));
        UserDTO user2 = new UserDTO(2L, "c", "d", new BigDecimal(2));
        List<UserDTO> users = List.of(user1, user2);
        when(userService.findAll()).thenReturn(users);

        ResponseEntity<List<UserDTO>> response = userController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
        verify(userService, times(1)).findAll();
    }

    @Test
    void findAll_noUsers_returnsEmptyListBody() {
        when(userService.findAll()).thenReturn(List.of());

        ResponseEntity<List<UserDTO>> response = userController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(userService, times(1)).findAll();
    }

    // ---------- create ----------
    @Test
    void create_validRequest_returnsCreatedUserBody() {
        UserRequestDTO request = new UserRequestDTO("firstName", "lastName");
        UserDTO dto = new UserDTO(1L, "firstName", "lastName", BigDecimal.ZERO);
        when(userService.create(request)).thenReturn(dto);

        ResponseEntity<UserDTO> response = userController.create(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(dto, response.getBody());
        verify(userService, times(1)).create(request);
    }

    @Test
    void create_serviceRejectsBody_propagatesException() {
        UserRequestDTO request = new UserRequestDTO("", "lastName");
        when(userService.create(request)).thenThrow(new UserModelValidityBreachException("First or last name are invalid"));

        assertThrows(UserModelValidityBreachException.class, () -> userController.create(request));
        verify(userService, times(1)).create(request);
    }

    // ---------- replaceById ----------
    @Test
    void replaceById_validRequest_returnsUpdatedUserBody() {
        Long id = 1L;
        UserRequestDTO request = new UserRequestDTO("new", "name");
        UserDTO dto = new UserDTO(id, "new", "name", new BigDecimal("50"));
        when(userService.replaceById(id, request)).thenReturn(dto);

        ResponseEntity<UserDTO> response = userController.replaceById(id, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
        verify(userService, times(1)).replaceById(id, request);
    }

    @Test
    void replaceById_unknownId_propagatesException() {
        Long id = 1L;
        UserRequestDTO request = new UserRequestDTO("new", "name");
        when(userService.replaceById(id, request)).thenThrow(new UserNotFoundException(id));

        assertThrows(UserNotFoundException.class, () -> userController.replaceById(id, request));
        verify(userService, times(1)).replaceById(id, request);
    }

    @Test
    void replaceById_serviceRejectsBody_propagatesException() {
        Long id = 1L;
        UserRequestDTO request = new UserRequestDTO("", "name");
        when(userService.replaceById(id, request)).thenThrow(new UserModelValidityBreachException("FirstName:  is invalid"));

        assertThrows(UserModelValidityBreachException.class, () -> userController.replaceById(id, request));
        verify(userService, times(1)).replaceById(id, request);
    }

    // ---------- deleteById ----------
    @Test
    void deleteById_existingId_returnsBodyWithoutId() {
        Long id = 1L;
        UserDeleteDTO dto = new UserDeleteDTO("firstName", "lastName", BigDecimal.ONE);
        when(userService.deleteById(id)).thenReturn(dto);

        ResponseEntity<UserDeleteDTO> response = userController.deleteById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
        verify(userService, times(1)).deleteById(id);
    }

    @Test
    void deleteById_unknownId_propagatesException() {
        Long id = 1L;
        when(userService.deleteById(id)).thenThrow(new UserNotFoundException(id));

        assertThrows(UserNotFoundException.class, () -> userController.deleteById(id));
        verify(userService, times(1)).deleteById(id);
    }

    // ---------- depositBalanceById ----------
    @Test
    void deposit_validAmount_returnsUpdatedUserBody() {
        Long id = 1L;
        BigInteger amount = BigInteger.valueOf(20);
        int decimals = 50;
        UserDTO dto = new UserDTO(id, "firstName", "lastName", new BigDecimal("20.50"));
        when(userService.depositBalanceById(id, amount, decimals)).thenReturn(dto);

        ResponseEntity<UserDTO> response = userController.depositBalanceById(id, amount, decimals);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
        verify(userService, times(1)).depositBalanceById(id, amount, decimals);
    }

    @Test
    void deposit_unknownUser_propagatesException() {
        Long id = 1L;
        BigInteger amount = BigInteger.valueOf(20);
        int decimals = 0;
        when(userService.depositBalanceById(id, amount, decimals)).thenThrow(new UserNotFoundException(id));

        assertThrows(UserNotFoundException.class, () -> userController.depositBalanceById(id, amount, decimals));
        verify(userService, times(1)).depositBalanceById(id, amount, decimals);
    }

    // ---------- withDrawById ----------
    @Test
    void withDraw_validAmount_returnsUpdatedUserBody() {
        Long id = 1L;
        BigInteger amount = BigInteger.valueOf(20);
        int decimals = 50;
        UserDTO dto = new UserDTO(id, "firstName", "lastName", new BigDecimal("29.50"));
        when(userService.withDrawById(id, amount, decimals)).thenReturn(dto);

        ResponseEntity<UserDTO> response = userController.withDrawById(id, amount, decimals);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
        verify(userService, times(1)).withDrawById(id, amount, decimals);
    }

    @Test
    void withDraw_unknownUser_propagatesException() {
        Long id = 1L;
        BigInteger amount = BigInteger.valueOf(20);
        int decimals = 0;
        when(userService.withDrawById(id, amount, decimals)).thenThrow(new UserNotFoundException(id));

        assertThrows(UserNotFoundException.class, () -> userController.withDrawById(id, amount, decimals));
        verify(userService, times(1)).withDrawById(id, amount, decimals);
    }
}
