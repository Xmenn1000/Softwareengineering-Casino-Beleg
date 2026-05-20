package casino.banking.services.user;

import casino.banking.view.user.response.UserDTO;

import java.util.List;

public interface UserQueryService {
    UserDTO findById(Long id);

    List<UserDTO> findAll();
}
