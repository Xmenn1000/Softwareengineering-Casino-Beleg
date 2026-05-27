package casino.banking.view.user.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.boot.internal.Abstract;

@Getter
@AllArgsConstructor
public class UserRequestDTO {
    String firstName;
    String lastName;
}
