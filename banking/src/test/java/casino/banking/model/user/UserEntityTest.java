package casino.banking.model.user;

import casino.banking.exceptions.user.UserModelValidityBreachException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityTest {

    // ---------- createUserEntity ----------
    @Test
    void createUserEntity_validNames_createsEntity() {
        String validFirstname = "firstName";
        String validLastName = "lastName";

        UserEntity result = assertDoesNotThrow(
                () -> UserEntity.createUserEntity(validFirstname, validLastName));

        assertEquals(validFirstname, result.getFirstName());
        assertEquals(validLastName, result.getLastName());
        assertNull(result.getId());
    }

    @Test
    void createUserEntity_initialBalanceIsZero() {
        String validFirstname = "firstName";
        String validLastName = "lastName";

        UserEntity result = assertDoesNotThrow(
                () -> UserEntity.createUserEntity(validFirstname, validLastName));

        assertEquals(BigDecimal.ZERO, result.getBalance());
    }

    @Test
    void createUserEntity_nullFirstName_throws() {
        String validFirstname = null;
        String validLastName = "lastName";

        assertThrows(UserModelValidityBreachException.class,
                () -> UserEntity.createUserEntity(validFirstname, validLastName));
    }

    @Test
    void createUserEntity_blankFirstName_throws() {
        String validFirstname = "";
        String validLastName = "lastName";

        assertThrows(UserModelValidityBreachException.class,
                () -> UserEntity.createUserEntity(validFirstname, validLastName));
    }

    @Test
    void createUserEntity_nullLastName_throws() {
        String validFirstname = "firstName";
        String validLastName = null;

        assertThrows(UserModelValidityBreachException.class,
                () -> UserEntity.createUserEntity(validFirstname, validLastName));
    }

    @Test
    void createUserEntity_blankLastName_throws() {
        String validFirstname = "firstName";
        String validLastName = "";

        assertThrows(UserModelValidityBreachException.class,
                () -> UserEntity.createUserEntity(validFirstname, validLastName));
    }

    // ---------- isValidName ----------
    @Test
    void isValidName_validName_returnsTrue() {
        assertTrue(UserEntity.isValidName("someName"));
    }

    @Test
    void isValidName_nullOrBlank_returnsFalse() {
        assertFalse(UserEntity.isValidName(null));
        assertFalse(UserEntity.isValidName(""));
        assertFalse(UserEntity.isValidName(" "));
    }

    // ---------- setFirstName ----------
    @Test
    void setFirstName_valid_updatesFirstName() {
        UserEntity user = UserEntity.createUserEntity("firstName", "lastName");

        user.setFirstName("newFirstName");

        assertEquals("newFirstName", user.getFirstName());
    }

    @Test
    void setFirstName_blank_throws() {
        UserEntity user = UserEntity.createUserEntity("firstName", "lastName");

        assertThrows(UserModelValidityBreachException.class, () -> user.setFirstName(""));
    }

    // ---------- setLastName ----------
    @Test
    void setLastName_valid_updatesLastName() {
        UserEntity user = UserEntity.createUserEntity("firstName", "lastName");

        user.setLastName("newLastName");

        assertEquals("newLastName", user.getLastName());
    }

    @Test
    void setLastName_blank_throws() {
        UserEntity user = UserEntity.createUserEntity("firstName", "lastName");

        assertThrows(UserModelValidityBreachException.class, () -> user.setLastName(""));
    }

    // ---------- addBalance ----------
    @Test
    void addBalance_positiveAmount_increasesBalance() {
        UserEntity user = UserEntity.createUserEntity("firstName", "lastName");

        user.addBalance(new BigDecimal("50"));

        assertEquals(0, new BigDecimal("50").compareTo(user.getBalance()));
    }

    @Test
    void addBalance_negativeAmount_decreasesBalance() {
        UserEntity user = UserEntity.createUserEntity("firstName", "lastName");

        user.addBalance(new BigDecimal("50"));
        user.addBalance(new BigDecimal("-20"));

        assertEquals(0, new BigDecimal("30").compareTo(user.getBalance()));
    }

    @Test
    void addBalance_null_throws() {
        UserEntity user = UserEntity.createUserEntity("firstName", "lastName");

        assertThrows(UserModelValidityBreachException.class, () -> user.addBalance(null));
    }

    // ---------- withDrawBalance ----------
    @Test
    void withDrawBalance_positiveAmount_decreasesBalance() {
        UserEntity user = UserEntity.createUserEntity("firstName", "lastName");

        user.addBalance(new BigDecimal("50"));
        user.withDrawBalance(new BigDecimal("20"));

        assertEquals(0, new BigDecimal("30").compareTo(user.getBalance()));
    }

    @Test
    void withDrawBalance_moreThanBalance_goesNegative() {
        UserEntity user = UserEntity.createUserEntity("firstName", "lastName");

        user.withDrawBalance(new BigDecimal("20"));

        assertEquals(0, new BigDecimal("-20").compareTo(user.getBalance()));
    }

    @Test
    void withDrawBalance_null_throws() {
        UserEntity user = UserEntity.createUserEntity("firstName", "lastName");

        assertThrows(UserModelValidityBreachException.class, () -> user.withDrawBalance(null));
    }
}
