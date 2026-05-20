package casino.banking.services.user;

import casino.banking.repository.user.UserRepository;
import casino.banking.view.user.request.UserRequestDTO;
import casino.banking.view.user.response.UserDTO;
import casino.banking.view.user.response.UserDeleteDTO;

import java.math.BigDecimal;
import java.util.List;

public interface UserService extends UserBalanceService, UserManagementService, UserQueryService {
}