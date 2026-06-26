package casino.roulette.model;

import casino.roulette.util.BetType;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;

@Entity
@Getter
@Table(name = "roulette_games")
public class RouletteGameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long user;

    @Column(name = "winning", nullable = false)
    private boolean winning;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "bet_amount", nullable = false)
    private BigDecimal betAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "bet_type", nullable = false)
    private BetType betType;

    @Column(name = "bet_value", nullable = false)
    private String betValue;

    @Column(name = "ball_position", nullable = false)
    private int ballPosition;

    protected RouletteGameEntity() {
    }

    public static RouletteGameEntity create(
            Long user,
            boolean winning,
            BigDecimal amount,
            BigDecimal betAmount,
            BetType betType,
            String betValue,
            int ballPosition
    ) {
        RouletteGameEntity entity = new RouletteGameEntity();
        entity.user = user;
        entity.winning = winning;
        entity.amount = amount;
        entity.betAmount = betAmount;
        entity.betType = betType;
        entity.betValue = betValue;
        entity.ballPosition = ballPosition;
        return entity;
    }
}
