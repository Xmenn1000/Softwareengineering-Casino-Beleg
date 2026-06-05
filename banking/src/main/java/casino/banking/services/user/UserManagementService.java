package casino.banking.services.user;

import casino.banking.request.user.UserRequestDTO;
import casino.banking.view.user.UserDTO;
import casino.banking.view.user.UserDeleteDTO;

public interface UserManagementService {
    UserDTO create(UserRequestDTO userRequest);

    UserDTO replaceById(Long id, UserRequestDTO userRequest);

    UserDeleteDTO deleteById(Long id);

}
