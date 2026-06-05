package casino.banking.model.transaction;

import casino.banking.model.user.UserEntity;
import casino.banking.util.GameService;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.math.BigDecimal;

@Entity
@Getter
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "invoicingParty", nullable = false)
    private GameService invoicingParty;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount = BigDecimal.ZERO;

    protected TransactionEntity() {
    }

    static public TransactionEntity createTransaction(GameService service, Long userId, BigDecimal amount) {
        TransactionEntity entity = new TransactionEntity();
        entity.invoicingParty = service;
        entity.userId = userId;
        entity.amount = amount;

        return entity;
    }

}
