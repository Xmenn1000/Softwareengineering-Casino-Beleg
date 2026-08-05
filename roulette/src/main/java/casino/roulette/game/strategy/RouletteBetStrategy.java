package casino.roulette.game.strategy;

import casino.roulette.util.BetType;

import java.math.BigDecimal;
import java.math.MathContext;

public interface RouletteBetStrategy {

    BetType betType();

    boolean isWinning(String betValue, int ballPosition);

    int payoutMultiplier();

    int winningOutcomes();

    default int totalOutcomes() {
        return 37;
    }

    default BigDecimal hitProbability() {
        return BigDecimal.valueOf(winningOutcomes())
                .divide(BigDecimal.valueOf(totalOutcomes()), MathContext.DECIMAL64);
    }

    default BigDecimal returnToPlayer() {
        return hitProbability()
                .multiply(BigDecimal.valueOf(payoutMultiplier() + 1));
    }

    default BigDecimal houseEdge() {
        return BigDecimal.ONE.subtract(returnToPlayer());
    }
}
