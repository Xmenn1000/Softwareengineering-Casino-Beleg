package casino.banking.controller.user;

import casino.banking.handler.user.CustomerNotFoundExeption;
import casino.banking.view.user.response.UserDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/casino/bank/api")
public class UserController {

    @GetMapping("/user/{id}")
    public UserDTO getUser(
            @PathVariable Long id
    ) {
        System.out.println(id);
        throw new CustomerNotFoundExeption(1L);
//        return new UserDTO(id, "Nils", "S", BigDecimal.valueOf(20));
    }

}
