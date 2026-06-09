package casino.banking.controller.user;

import casino.banking.request.user.UserRequestDTO;
import casino.banking.services.user.UserService;
import casino.banking.view.user.UserDTO;
import casino.banking.view.user.UserDeleteDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;

@RestController
public class UserController implements UserApi {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<UserDTO> findById(Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @Override
    public ResponseEntity<List<UserDTO>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @Override
    public ResponseEntity<UserDTO> create(UserRequestDTO userRequest) {
        return ResponseEntity.ok(userService.create(userRequest));
    }

    @Override
    public ResponseEntity<UserDTO> replaceById(Long id, UserRequestDTO userRequest) {
        return ResponseEntity.ok(userService.replaceById(id, userRequest));
    }

    @Override
    public ResponseEntity<UserDeleteDTO> deleteById(Long id) {
        return ResponseEntity.ok(userService.deleteById(id));
    }

    @Override
    public ResponseEntity<UserDTO> depositBalanceById(Long userId, BigInteger amount, int decimals) {
        return ResponseEntity.ok(userService.depositBalanceById(userId, amount, decimals));
    }
}
