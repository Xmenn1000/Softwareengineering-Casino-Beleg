package casino.banking.model.user;

public interface UserFactory {

    UserEntity createUser(String firstName, String lastName);
}
