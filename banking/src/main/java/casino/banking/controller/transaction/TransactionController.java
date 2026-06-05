package casino.banking.controller.transaction;

import casino.banking.requestClients.UserRestClient;
import casino.banking.util.GameService;
import casino.banking.request.transaction.TransactionRequestDTO;
import casino.banking.request.transaction.UserGameTransactionRequestDTO;
import casino.banking.request.transaction.UserTransactionRequestDTO;
import casino.banking.view.transaction.TransactionDTO;
import casino.banking.view.transaction.UserTransactionDTO;
import casino.banking.view.user.UserDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

@Log4j2
@RestController
public class TransactionController implements TransactionApi {

    private final UserRestClient userRestClient;

    TransactionController(UserRestClient userRestClient) {
        this.userRestClient = userRestClient;
    }

    @Override
    public ResponseEntity<List<UserTransactionDTO>> findAll() {
        Logger log = Logger.getLogger(TransactionController.class.getName());

        UserDTO user = userRestClient.getUserById(100L);
        log.info(user.toString());

        List<UserTransactionDTO> transactions = List.of();

        return ResponseEntity.ok(transactions);
    }

    @Override
    public ResponseEntity<List<TransactionDTO>> findByUserId(Long id) {
        List<TransactionDTO> transactions;
        TransactionDTO trans1 = new TransactionDTO(id, new BigDecimal(2000));
        TransactionDTO trans2 = new TransactionDTO(id, new BigDecimal(2000));
        transactions = List.of(trans1, trans2);
        return ResponseEntity.ok(transactions);
    }

    @Override
    public ResponseEntity<UserTransactionRequestDTO> createForUserId(Long userId, TransactionRequestDTO transactionRequestDTO) {
        return ResponseEntity.ok(
                new UserTransactionRequestDTO(
                        userId,
                        1L
                        , transactionRequestDTO
                ));
    }

    @Override
    public ResponseEntity<UserTransactionRequestDTO> replaceById(Long transactionId, UserGameTransactionRequestDTO userGameTransactionRequestDTO) {
        return ResponseEntity.ok(
                new UserTransactionRequestDTO(
                        userGameTransactionRequestDTO.getUserId(),
                        transactionId,
                        new TransactionRequestDTO(
                                GameService.SLOTS,
                                BigDecimal.valueOf(2000)
                        )
                ));
    }

    @Override
    public ResponseEntity<UserGameTransactionRequestDTO> deleteById(Long transactionId, UserTransactionRequestDTO userGameTransactionRequestDTO) {
        return ResponseEntity.ok(
                new UserGameTransactionRequestDTO(
                        userGameTransactionRequestDTO.getUserId(),
                        new TransactionRequestDTO(
                                GameService.SLOTS,
                                BigDecimal.valueOf(2000)
                        )
                ));
    }

}
