package casino.banking.services.user;

import casino.banking.handler.user.UserNotFoundExeption;
import casino.banking.model.user.UserEntity;
import casino.banking.repository.user.UserRepository;
import casino.banking.view.user.request.UserRequestDTO;
import casino.banking.view.user.response.UserDTO;
import casino.banking.view.user.response.UserDeleteDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundExeption(id));
        return user.toUserDTO();
    }

    @Override
    public List<UserDTO> findAll() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream().map(UserEntity::toUserDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO create(UserRequestDTO userRequest) {
        UserEntity user = UserEntity.createUserEntity(userRequest.firstName(), userRequest.lastName());
        return userRepository.save(user).toUserDTO();
    }

    @Override
    public UserDTO replaceById(Long id, UserRequestDTO userRequest) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new UserNotFoundExeption(id));
        UserEntity user = UserEntity.createUserEntity(userRequest.firstName(), userRequest.lastName());
        return null;
    }

    @Override
    public UserDeleteDTO deleteById(Long id) {
        return null;
    }

    @Override
    public UserDTO depositBalanceById(Long userId, BigDecimal amount, int decimals) {
        return null;
    }
}
