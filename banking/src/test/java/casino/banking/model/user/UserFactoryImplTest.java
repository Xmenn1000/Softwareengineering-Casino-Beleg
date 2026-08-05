package casino.banking.model.user;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class UserFactoryImplTest {

    @Test
    void createUser_validUser_createsUser() {
        String firstName = "firstName";
        String lastName = "lastName";

        UserFactory factory = new UserFactoryImpl();
        User createdUser = factory.createUser(firstName, lastName);

        assertEquals(firstName, createdUser.getFirstName());
        assertEquals(lastName, createdUser.getLastName());
        assertEquals(BigDecimal.ZERO, createdUser.getBalance());
        assertNull(createdUser.getId());
    }
}
