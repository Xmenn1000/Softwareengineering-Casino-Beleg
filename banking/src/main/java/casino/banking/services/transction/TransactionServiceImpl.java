package casino.banking.services.transction;

import casino.banking.request.transaction.TransactionRequestDTO;
import casino.banking.request.transaction.UserGameTransactionRequestDTO;
import casino.banking.request.transaction.UserTransactionRequestDTO;
import casino.banking.view.transaction.TransactionDTO;
import casino.banking.view.transaction.UserTransactionDTO;

import java.util.List;

public class TransactionServiceImpl {

    public List<UserTransactionDTO> findAll() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public TransactionDTO findById(Long transactionId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public List<TransactionDTO> findAllByUserId(Long userId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public UserTransactionRequestDTO create(Long userId, TransactionRequestDTO request) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public UserTransactionRequestDTO update(Long transactionId, UserGameTransactionRequestDTO request) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public UserGameTransactionRequestDTO delete(Long transactionId, UserTransactionRequestDTO request) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
