package casino.banking.model.user;

import casino.banking.exceptions.user.ModelValidityBreachException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.math.BigDecimal;

@Entity
@Getter
@Table(name = "users")
public class UserEntity implements User{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    protected UserEntity() {
    }

    static public UserEntity createUserEntity(String firstName, String lastName) {
        if(!isValidName(firstName) || !isValidName(lastName)) {
            throw new ModelValidityBreachException("First or last name are invalid");
        }
        UserEntity entity = new UserEntity();
        entity.firstName = firstName;
        entity.lastName = lastName;
        return entity;
    }

    static public boolean isValidName(String name) {
        return name != null && !name.isBlank();
    }


    public void setFirstName(String firstName) {
        if(!isValidName(firstName)) {
            throw new ModelValidityBreachException(String.format("FirstName: %s is invalid", firstName));
        }
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        if(!isValidName(lastName)) {
            throw new ModelValidityBreachException(String.format("LastName: %s is invalid", lastName));
        }
        this.lastName = lastName;
    }

    public void addBalance(BigDecimal amount) {
        try {
            this.balance = this.balance.add(amount);
        }
        catch(NullPointerException ex) {
            throw new ModelValidityBreachException(amount.toString(), ex);
        }
    }

}
