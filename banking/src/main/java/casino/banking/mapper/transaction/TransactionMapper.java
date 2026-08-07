package casino.banking.mapper.transaction;

import casino.banking.model.transaction.TransactionEntity;
import casino.banking.view.transaction.TransactionDTO;
import casino.banking.view.transaction.TransactionDeleteDTO;
import casino.banking.view.transaction.UserTransactionDTO;

public final class TransactionMapper {

    private TransactionMapper() {
    }

    public static TransactionDTO toDto(TransactionEntity transaction) {
        return new TransactionDTO(
                transaction.getId(),
                transaction.getInvoicingParty(),
                transaction.getAmount()
        );
    }

    public static UserTransactionDTO toUserTransactionDto(TransactionEntity transaction) {
        return new UserTransactionDTO(transaction.getUserId(), toDto(transaction));
    }

    public static TransactionDeleteDTO toTransactionDeleteDTO(TransactionEntity transaction) {
        return new TransactionDeleteDTO(transaction.getInvoicingParty(), transaction.getUserId(), transaction.getAmount());
    }
}
