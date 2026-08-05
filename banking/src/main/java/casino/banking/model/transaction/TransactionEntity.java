package casino.banking.model.transaction;

import casino.banking.exceptions.transaction.TransactionModelValidityBreachException;
import casino.banking.util.GameService;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.math.BigDecimal;

@Entity
@Getter
@Table(name = "transactions")
public class TransactionEntity implements Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "invoicingParty", nullable = false)
    private GameService invoicingParty;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount = BigDecimal.ZERO;

    protected TransactionEntity() {
    }

    public static TransactionEntity createTransaction(GameService service, Long userId, BigDecimal amount) {
        TransactionEntity entity = new TransactionEntity();
        validate(service, userId, amount);
        entity.invoicingParty = service;
        entity.userId = userId;
        entity.amount = amount;

        return entity;
    }
    private static void validate(GameService service, Long userId, BigDecimal amount) {
        if (service == null) {
            throw new TransactionModelValidityBreachException("invoicingParty must not be null");
        }
        if (userId == null) {
            throw new TransactionModelValidityBreachException("userId must not be null");
        }
        if (amount == null) {
            throw new TransactionModelValidityBreachException("amount must not be null");
        }
    }


    public void replace(GameService service, Long userId, BigDecimal amount) {
        validate(service, userId, amount);
        this.invoicingParty = service;
        this.userId = userId;
        this.amount = amount;
    }
}
