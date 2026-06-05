package casino.banking.services.transction;

import casino.banking.mapper.TransactionMapper;
import casino.banking.model.transaction.TransactionEntity;
import casino.banking.repository.transaction.TransactionRepository;
import casino.banking.request.transaction.TransactionRequestDTO;
import casino.banking.request.transaction.UserGameTransactionRequestDTO;
import casino.banking.request.transaction.UserTransactionRequestDTO;
import casino.banking.view.transaction.TransactionDTO;
import casino.banking.view.transaction.UserTransactionDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public UserTransactionDTO createForUserId(Long userId, TransactionRequestDTO transactionRequestDTO) {
        throw new UnsupportedOperationException();
    }

    @Override
    public UserTransactionDTO replaceById(Long transactionId, UserGameTransactionRequestDTO userGameTransactionRequestDTO) {
        throw new UnsupportedOperationException();
    }

    @Override
    public UserTransactionDTO deleteById(Long transactionId, UserTransactionRequestDTO userTransactionRequestDTO) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<UserTransactionDTO> findAll() {
        return transactionRepository.findAll().stream().map(TransactionMapper::toUserTransactionDto).toList();
    }

    @Override
    public List<TransactionDTO> findByUserId(Long userId) {
        return transactionRepository.findByUserId(userId);
    }
}
