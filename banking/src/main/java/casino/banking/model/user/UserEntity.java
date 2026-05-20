package casino.banking.model.user;

import casino.banking.view.user.response.UserDTO;
import casino.banking.view.user.response.UserDeleteDTO;
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
public class UserEntity {
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
        if(!isValidName(firstName) && !isValidName(lastName)) {
            throw new IllegalArgumentException("First and last name are invalid");
        }
        UserEntity entity = new UserEntity();
        entity.firstName = firstName;
        entity.lastName = lastName;
        return entity;
    }

    static public boolean isValidName(String name) {
        return true;
    }

    public UserDTO toUserDTO() {
        return new UserDTO(this.id, this.firstName, this.lastName, this.balance);
    }

    public UserDeleteDTO UserDeleteDTO() {
        return new UserDeleteDTO(this.firstName, this.lastName, this.balance);
    }

}
