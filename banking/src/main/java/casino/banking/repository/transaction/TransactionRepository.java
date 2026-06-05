package casino.banking.repository.transaction;

import casino.banking.model.transaction.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {


    @Query("""
            select t from TransactionEntity t where t.userId = :userId
            """)
    List<TransactionEntity> findByUserId(@Param("userId") Long userId);
}
