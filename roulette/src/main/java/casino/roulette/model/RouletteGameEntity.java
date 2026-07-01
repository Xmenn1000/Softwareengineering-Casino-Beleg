package casino.roulette.model;

import casino.roulette.exceptions.BadRouletteRequestException;
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
        validateUser(user);
        validateAmount(amount);
        validateBetAmount(betAmount);
        validateBetType(betType);
        validateBetValue(betValue);
        validateBallPosition(ballPosition);

        RouletteGameEntity entity = new RouletteGameEntity();
        entity.user = user;
        entity.winning = winning;
        entity.amount = amount;
        entity.betAmount = betAmount;
        entity.betType = betType;
        entity.betValue = betValue.trim().toUpperCase();
        entity.ballPosition = ballPosition;
        return entity;
    }

    private static void validateUser(Long user) {
        if (user == null) {
            throw new BadRouletteRequestException("User must not be empty");
        }
    }

    private static void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new BadRouletteRequestException("Amount must not be empty");
        }
    }

    private static void validateBetAmount(BigDecimal betAmount) {
        if (betAmount == null || betAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRouletteRequestException("Bet amount must be greater than 0");
        }
    }

    private static void validateBetType(BetType betType) {
        if (betType == null) {
            throw new BadRouletteRequestException("Bet type must not be empty");
        }
    }

    private static void validateBetValue(String betValue) {
        if (betValue == null || betValue.isBlank()) {
            throw new BadRouletteRequestException("Bet value must not be empty");
        }
    }

    private static void validateBallPosition(int ballPosition) {
        if (ballPosition < 0 || ballPosition > 36) {
            throw new BadRouletteRequestException("Ball position must be between 0 and 36");
        }
    }
}
