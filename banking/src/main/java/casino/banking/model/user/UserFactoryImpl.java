package casino.banking.model.user;

import org.springframework.stereotype.Service;

@Service
public class UserFactoryImpl implements UserFactory{

    @Override
    public UserEntity createUser(String firstName, String lastName) {
        return UserEntity.createUserEntity(firstName, lastName);
    }
}
