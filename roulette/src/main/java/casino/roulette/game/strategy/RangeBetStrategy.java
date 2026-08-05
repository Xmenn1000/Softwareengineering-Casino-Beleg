package casino.roulette.game.strategy;

import casino.roulette.exceptions.BadRouletteRequestException;
import casino.roulette.util.BetType;
import org.springframework.stereotype.Component;

@Component
public class RangeBetStrategy extends RouletteBetStrategySupport {

    @Override
    public BetType betType() {
        return BetType.RANGE;
    }

    @Override
    public boolean isWinning(String betValue, int ballPosition) {
        validateBallPosition(ballPosition);

        if (ballPosition == 0) {
            return false;
        }

        String normalizedBetValue = normalize(betValue);

        if (!normalizedBetValue.equals("LOW") && !normalizedBetValue.equals("HIGH")) {
            throw new BadRouletteRequestException("RANGE bet value must be LOW or HIGH");
        }

        boolean ballIsLow = ballPosition >= 1 && ballPosition <= 18;

        return normalizedBetValue.equals("LOW") == ballIsLow;
    }

    @Override
    public int payoutMultiplier() {
        return 1;
    }

    @Override
    public int winningOutcomes() {
        return 18;
    }
}
