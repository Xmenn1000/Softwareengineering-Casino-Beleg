package casino.banking.services.user;

import casino.banking.exceptions.UserNotFoundException;
import casino.banking.mapper.UserMapper;
import casino.banking.model.user.UserEntity;
import casino.banking.repository.user.UserRepository;
import casino.banking.request.user.UserRequestDTO;
import casino.banking.util.MoneyHelper;
import casino.banking.view.user.UserDTO;
import casino.banking.view.user.UserDeleteDTO;
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
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return UserMapper.toDto(user);
    }

    @Override
    public List<UserDTO> findAll() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public UserDTO create(UserRequestDTO userRequest) {
        UserEntity user = UserEntity.createUserEntity(userRequest.getFirstName(), userRequest.getLastName());
        return UserMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDTO replaceById(Long id, UserRequestDTO userRequest) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        userEntity.setFirstName(userRequest.getFirstName());
        userEntity.setLastName(userRequest.getLastName());
        userRepository.save(userEntity);
        return UserMapper.toDto(userEntity);
    }

    @Override
    public UserDeleteDTO deleteById(Long id) {
        UserEntity userToDelete = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(userToDelete);
        return UserMapper.toDeleteDto(userToDelete);
    }

    @Override
    public UserDTO depositBalanceById(Long id, BigDecimal amount, int decimals) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        BigDecimal toAdd = MoneyHelper.createBigDecimal(amount, decimals);
        user.addBalance(toAdd);
        return UserMapper.toDto(user);
    }
}