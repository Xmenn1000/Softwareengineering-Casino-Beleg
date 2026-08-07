package casino.banking.services.transction;

import casino.banking.exceptions.transaction.TransactionNotKnownException;
import casino.banking.mapper.transaction.TransactionMapper;
import casino.banking.model.transaction.TransactionEntity;
import casino.banking.model.transaction.TransactionFactory;
import casino.banking.repository.transaction.TransactionRepository;
import casino.banking.request.transaction.TransactionRequestDTO;
import casino.banking.request.transaction.UserGameTransactionRequestDTO;
import casino.banking.requestClients.transaction.UserRestClient;
import casino.banking.util.MoneyHelper;
import casino.banking.view.transaction.TransactionDTO;
import casino.banking.view.transaction.TransactionDeleteDTO;
import casino.banking.view.transaction.UserTransactionDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Transactional
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRestClient userRestClient;
    private final TransactionFactory transactionFactory;

    TransactionServiceImpl(TransactionRepository transactionRepository, UserRestClient userRestClient, TransactionFactory transactionFactory) {
        this.transactionRepository = transactionRepository;
        this.userRestClient = userRestClient;
        this.transactionFactory = transactionFactory;
    }

    @Override
    public UserTransactionDTO createForUserId(Long userId, TransactionRequestDTO transactionRequestDTO) {
        BigDecimal requestedAmount = transactionRequestDTO.getAmount();

        // Deposit and withdraw both take a positive amount, so we always split the absolute value.
        BigDecimal magnitude = requestedAmount.abs();
        BigInteger amount = MoneyHelper.extractIntegerPart(magnitude);
        int decimals = MoneyHelper.extractFractionPart2Decimals(magnitude);

        if(requestedAmount.signum() >= 0) {
            userRestClient.depositBalanceById(userId, amount, decimals);
        }
        else {
            userRestClient.withDrawById(userId, amount, decimals);
        }

        TransactionEntity transactionEntity = transactionFactory.createTransaction(transactionRequestDTO.getInvoicingParty(), userId, transactionRequestDTO.getAmount());
        transactionRepository.save(transactionEntity);
        return TransactionMapper.toUserTransactionDto(transactionEntity);
    }

    @Override
    public UserTransactionDTO replaceById(Long transactionId, UserGameTransactionRequestDTO userGameTransactionRequestDTO) {
        TransactionEntity transactionToReplace = transactionRepository.findById(transactionId).orElseThrow(() -> new TransactionNotKnownException(transactionId));

        userRestClient.findById(userGameTransactionRequestDTO.getUserId());

        transactionToReplace.replace(
                userGameTransactionRequestDTO.getTransactionRequestDTO().getInvoicingParty(),
                userGameTransactionRequestDTO.getUserId(),
                userGameTransactionRequestDTO.getTransactionRequestDTO().getAmount());

        return TransactionMapper.toUserTransactionDto(transactionToReplace);
    }

    @Override
    public TransactionDeleteDTO deleteById(Long transactionId) {
        TransactionEntity transactionToDelete = transactionRepository.findById(transactionId).orElseThrow(() -> new TransactionNotKnownException(transactionId));
        transactionRepository.deleteById(transactionId);
        return TransactionMapper.toTransactionDeleteDTO(transactionToDelete);
    }

    @Override
    public List<UserTransactionDTO> findAll() {
        return transactionRepository.findAll().stream().map(TransactionMapper::toUserTransactionDto).toList();
    }

    @Override
    public List<TransactionDTO> findByUserId(Long userId) {
        userRestClient.findById(userId);
        return transactionRepository.findByUserId(userId).stream().map(TransactionMapper::toDto).toList();
    }
}
