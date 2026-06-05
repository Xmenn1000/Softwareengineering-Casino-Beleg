package casino.banking.services.transction;

import casino.banking.view.transaction.TransactionDTO;
import casino.banking.view.transaction.UserTransactionDTO;

import java.util.List;

public interface TransactionQueryService {

    List<UserTransactionDTO> findAll();

    List<TransactionDTO> findByUserId(Long userId);
}