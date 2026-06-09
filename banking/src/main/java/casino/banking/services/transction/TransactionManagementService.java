package casino.banking.services.transction;

import casino.banking.request.transaction.TransactionRequestDTO;
import casino.banking.request.transaction.UserGameTransactionRequestDTO;
import casino.banking.view.transaction.UserTransactionDTO;

public interface TransactionManagementService {

    UserTransactionDTO createForUserId(
            Long userId,
            TransactionRequestDTO transactionRequestDTO
    );

    UserTransactionDTO replaceById(
            Long transactionId,
            UserGameTransactionRequestDTO userGameTransactionRequestDTO
    );

    UserTransactionDTO deleteById(
            Long transactionId
    );
}