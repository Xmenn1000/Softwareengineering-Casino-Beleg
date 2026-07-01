package casino.slots.repository;

import casino.slots.model.SlotsGameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SlotsGameRepository extends JpaRepository<SlotsGameEntity, Long> {
  List<SlotsGameEntity> findByUser(long user);

  // restlichen Methoden werden durch JpaRepository geerbt

}
