package casino.roulette.repository;

import casino.roulette.model.RouletteGameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// erstellt DB nicht selbst (ist ja schon da) sondern erstellt die Tabellen innerhalb der DB
public interface RouletteGameRepository extends JpaRepository<RouletteGameEntity, Long> {
    List<RouletteGameEntity> findByUser(Long user);
}
