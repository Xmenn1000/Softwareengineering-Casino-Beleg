package casino.slots.repository;

import casino.slots.model.SlotsGameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SlotsGameRepository extends JpaRepository<SlotsGameEntity, Long> {
  List<SlotsGameEntity> findByUserId(long userId);
}
