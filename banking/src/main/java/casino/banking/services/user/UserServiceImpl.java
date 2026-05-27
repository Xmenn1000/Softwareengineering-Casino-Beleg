package casino.banking.services.user;

import casino.banking.exceptions.UserNotFoundExeption;
import casino.banking.model.user.UserEntity;
import casino.banking.repository.user.UserRepository;
import casino.banking.util.MoneyHelper;
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
        UserEntity user = UserEntity.createUserEntity(userRequest.getFirstName(), userRequest.getLastName());
        return userRepository.save(user).toUserDTO();
    }

    @Override
    public UserDTO replaceById(Long id, UserRequestDTO userRequest) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new UserNotFoundExeption(id));
        userEntity.setFirstName(userRequest.getFirstName());
        userEntity.setLastName(userRequest.getLastName());
        return userEntity.toUserDTO();
    }

    @Override
    public UserDeleteDTO deleteById(Long id) {
        UserEntity userToDelete = userRepository.findById(id).orElseThrow(() -> new UserNotFoundExeption(id));
        userRepository.delete(userToDelete);
        return userToDelete.toUserDeleteDTO();
    }

    @Override
    public UserDTO depositBalanceById(Long id, BigDecimal amount, int decimals) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundExeption(id));
        BigDecimal toAdd = MoneyHelper.createBigDecimal(amount, decimals);
        user.addBalance(toAdd);
        return user.toUserDTO();
    }
}
