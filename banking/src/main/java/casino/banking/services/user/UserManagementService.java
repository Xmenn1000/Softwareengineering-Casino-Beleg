package casino.banking.services.user;

import casino.banking.view.user.request.UserRequestDTO;
import casino.banking.view.user.response.UserDTO;
import casino.banking.view.user.response.UserDeleteDTO;

public interface UserManagementService {
    UserDTO create(UserRequestDTO userRequest);

    UserDTO replaceById(Long id, UserRequestDTO userRequest);

    UserDeleteDTO deleteById(Long id);

}
