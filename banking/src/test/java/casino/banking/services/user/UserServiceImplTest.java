package casino.banking.services.user;

import casino.banking.exceptions.user.UserNotFoundException;
import casino.banking.model.user.UserEntity;
import casino.banking.model.user.UserFactory;
import casino.banking.repository.user.UserRepository;
import casino.banking.request.user.UserRequestDTO;
import casino.banking.view.user.UserDTO;
import casino.banking.view.user.UserDeleteDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserFactory userFactory;


    @Test
    void findById_idExists_returnsValidDTO() {
        Long id = 1L;

        UserEntity user = UserEntity.createUserEntity("firstName", "lastName");
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserDTO expect = new UserDTO(null, "firstName", "lastName", BigDecimal.ZERO);

        UserDTO result = userService.findById(id);

        assertEquals(expect, result);
    }


    @Test
    void findById_idNotExists_throwsUserNotFoundException() {
        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findById(id));
    }

    @Test
    void findAll_oneUser_returnsUserInList() {
        UserEntity user = UserEntity.createUserEntity("firstName", "lastName");
        when(userRepository.findAll()).thenReturn(List.of(user));

        UserDTO expect = new UserDTO(null, "firstName", "lastName", BigDecimal.ZERO);

        List<UserDTO> result = userService.findAll();

        assertEquals(1, result.size());
        assertTrue(result.contains(expect));
    }

    @Test
    void findAll_noUser_returnsEmptyList() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<UserDTO> result = userService.findAll();

        assertTrue(result.isEmpty());
    }

    @Test
    void create_validRequest_savesAndReturnsDTO() {
        UserRequestDTO request = new UserRequestDTO("firstName", "lastName");

        UserEntity user = UserEntity.createUserEntity("firstName", "lastName");
        when(userFactory.createUser("firstName", "lastName")).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        UserDTO expect = new UserDTO(null, "firstName", "lastName", BigDecimal.ZERO);

        UserDTO result = userService.create(request);

        assertEquals(expect, result);
        verify(userFactory).createUser("firstName", "lastName");
    }

    @Test
    void replaceById_validRequest_updatesAndReturnsDTO() {
        Long id = 1L;
        UserRequestDTO request = new UserRequestDTO("newFirst", "newLast");
        UserEntity user = UserEntity.createUserEntity("firstName", "lastName");
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserDTO expect = new UserDTO(null, "newFirst", "newLast", BigDecimal.ZERO);

        UserDTO result = userService.replaceById(id, request);

        assertEquals(expect, result);
        verify(userRepository).save(user);
    }

    @Test
    void replaceById_idNotExists_throwsUserNotFoundException() {
        Long id = 1L;
        UserRequestDTO request = new UserRequestDTO("newFirst", "newLast");
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.replaceById(id, request));
    }

    @Test
    void deleteById_existingId_deletesAndReturnsDTO() {
        Long id = 1L;
        UserEntity user = UserEntity.createUserEntity("firstName", "lastName");
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserDeleteDTO result = userService.deleteById(id);

        assertEquals("firstName", result.getFirstName());
        assertEquals("lastName", result.getLastName());
        assertEquals(BigDecimal.ZERO, result.getBalance());
        verify(userRepository).delete(user);
    }

    @Test
    void deleteById_idNotExists_throwsUserNotFoundException() {
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteById(id));
    }

    @Test
    void depositBalanceById_validAmount_increasesBalanceAndReturnsDTO() {
        Long id = 1L;
        BigInteger amount = new BigInteger("20");
        int decimals = 50;
        UserEntity user = UserEntity.createUserEntity("firstName", "lastName");
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserDTO result = userService.depositBalanceById(id, amount, decimals);

        assertEquals("firstName", result.getFirstName());
        assertEquals("lastName", result.getLastName());
        assertEquals(0, new BigDecimal("20.50").compareTo(result.getBalance()));
    }

    @Test
    void depositBalanceById_idNotExists_throwsUserNotFoundException() {
        Long id = 1L;
        BigInteger amount = new BigInteger("20");
        int decimals = 50;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.depositBalanceById(id, amount, decimals));
    }
}
