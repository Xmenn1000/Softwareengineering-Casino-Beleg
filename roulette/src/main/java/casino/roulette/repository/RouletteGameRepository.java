package casino.roulette.repository;

import casino.roulette.model.RouletteGameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouletteGameRepository extends JpaRepository<RouletteGameEntity, Long> {
    List<RouletteGameEntity> findByUser(Long user);
}
