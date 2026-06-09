package casino.banking.services.user;

import casino.banking.model.user.UserEntity;
import casino.banking.repository.user.UserRepository;
import casino.banking.view.user.UserDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private UserEntity userEntity;

    @Test
    void findById_idExists_returnsValidDTO() {
        Long id = 1L;

        UserEntity user = UserEntity.createUserEntity("firstName", "lastName");

        try (MockedStatic<UserEntity> mockedUserEntity = mockStatic(UserEntity.class)) {
            mockedUserEntity
                    .when(() -> UserEntity.createUserEntity("firstName", "lastName"))
                    .thenReturn(user);

            when(userRepository.findById(id)).thenReturn(Optional.of(user));

            UserDTO expect = new UserDTO(1L, "firstName", "lastName", BigDecimal.ZERO);

            UserDTO result = userService.findById(id);

            assertEquals(expect, result);
        }
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