package casino.banking.mapper;

import casino.banking.model.user.UserEntity;
import casino.banking.view.user.UserDTO;
import casino.banking.view.user.UserDeleteDTO;

public final class UserMapper {

    private UserMapper() {
    }

    public static UserDTO toDto(UserEntity user) {
        return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getBalance());
    }

    public static UserDeleteDTO toDeleteDto(UserEntity user) {
        return new UserDeleteDTO(user.getFirstName(), user.getLastName(), user.getBalance());
    }
}
