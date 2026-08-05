package casino.banking.mapper.user;

import casino.banking.model.transaction.TransactionEntity;
import casino.banking.model.user.UserEntity;
import casino.banking.view.user.UserDTO;
import casino.banking.view.user.UserDeleteDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserMapperTest {

    private UserEntity dummyUser;

    @BeforeEach
    void setUp() {
        Long id = 1L;
        String firstName = "testFirstName";
        String lastName = "testLastName";
        BigDecimal balance = new BigDecimal(20);

        dummyUser =  mock(UserEntity.class);
        when(dummyUser.getId()).thenReturn(id);
        when(dummyUser.getFirstName()).thenReturn(firstName);
        when(dummyUser.getLastName()).thenReturn(lastName);
        when(dummyUser.getBalance()).thenReturn(balance);
    }

    @Test
    void toDto_valid_createsDTOWithFields() {
        UserDTO newUserDto = UserMapper.toDto(dummyUser);
        assertEquals(newUserDto.getId(), dummyUser.getId());
        assertEquals(newUserDto.getFirstName(), dummyUser.getFirstName());
        assertEquals(newUserDto.getLastName(), dummyUser.getLastName());
        assertEquals(newUserDto.getBalance(), dummyUser.getBalance());
    }

    @Test
    void toDeleteDto_valid_createsDTOWithFields() {
        UserDeleteDTO newUserDto = UserMapper.toDeleteDto(dummyUser);
        assertEquals(newUserDto.getFirstName(), dummyUser.getFirstName());
        assertEquals(newUserDto.getLastName(), dummyUser.getLastName());
        assertEquals(newUserDto.getBalance(), dummyUser.getBalance());
    }
}
