package casino.banking.services.transction;

import casino.banking.exceptions.TransactionNotKnownException;
import casino.banking.mapper.TransactionMapper;
import casino.banking.model.transaction.TransactionEntity;
import casino.banking.repository.transaction.TransactionRepository;
import casino.banking.request.transaction.TransactionRequestDTO;
import casino.banking.request.transaction.UserGameTransactionRequestDTO;
import casino.banking.view.transaction.TransactionDTO;
import casino.banking.view.transaction.UserTransactionDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Transactional
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public UserTransactionDTO createForUserId(Long userId, TransactionRequestDTO transactionRequestDTO) {
        TransactionEntity transactionEntity = TransactionEntity.createTransaction(transactionRequestDTO.getGameService(), userId, transactionRequestDTO.getAmount());
        transactionRepository.save(transactionEntity);
        return TransactionMapper.toUserTransactionDto(transactionEntity);
    }

    @Override
    public UserTransactionDTO replaceById(Long transactionId, UserGameTransactionRequestDTO userGameTransactionRequestDTO) {
        TransactionEntity transactionToReplace = transactionRepository.findById(transactionId).orElseThrow(() -> new TransactionNotKnownException(transactionId));

        transactionToReplace.replace(
                userGameTransactionRequestDTO.getTransactionRequestDTO().getGameService(),
                userGameTransactionRequestDTO.getUserId(),
                userGameTransactionRequestDTO.getTransactionRequestDTO().getAmount());

        return TransactionMapper.toUserTransactionDto(transactionToReplace);
    }

    @Override
    public UserTransactionDTO deleteById(Long transactionId) {
        TransactionEntity transactionToDelete = transactionRepository.findById(transactionId).orElseThrow(() -> new TransactionNotKnownException(transactionId));
        transactionRepository.deleteById(transactionId);
        return TransactionMapper.toUserTransactionDto(transactionToDelete);
    }

    @Override
    public List<UserTransactionDTO> findAll() {
        return transactionRepository.findAll().stream().map(TransactionMapper::toUserTransactionDto).toList();
    }

    @Override
    public List<TransactionDTO> findByUserId(Long userId) {
        return transactionRepository.findByUserId(userId).stream().map(TransactionMapper::toDto).toList();
    }
}