package casino.banking.requestClients;

import casino.banking.exceptions.UserNotFoundExeption;
import casino.banking.view.user.UserDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Log4j2
@Component
public class UserRestClient {

    private final RestClient restClient;

    public UserRestClient(@Qualifier("bankRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public UserDTO getUserById(Long id) {
        return restClient.get()
                .uri("/user/{id}", id)
                .retrieve()
                .onStatus(status -> status.value() == 404, (request, reponse) -> {
                    throw new UserNotFoundExeption(id);
                })
                .body(UserDTO.class);
    }

}
