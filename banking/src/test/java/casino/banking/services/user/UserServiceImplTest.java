package casino.banking.services.user;

import casino.banking.model.user.UserEntity;
import casino.banking.model.user.UserFactory;
import casino.banking.repository.user.UserRepository;
import casino.banking.view.user.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

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
        when(userFactory.createUser("firstName", "lastName")).thenReturn(user);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserDTO expect = new UserDTO(null, "firstName", "lastName", BigDecimal.ZERO);

        UserDTO result = userService.findById(id);

        assertEquals(expect, result);
    }


    @Test
    void findById_idNotExists_throwsUserNotFoundException() {
        fail();
    }

    @Test
    void findAll() {
        fail();
    }

    @Test
    void create() {
        fail();
    }

    @Test
    void replaceById() {
        fail();
    }

    @Test
    void deleteById() {
        fail();
    }

    @Test
    void depositBalanceById() {
        fail();
    }
}