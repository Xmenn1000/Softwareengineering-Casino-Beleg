package casino.roulette.game.strategy;

import casino.roulette.exceptions.BadRouletteRequestException;
import casino.roulette.util.BetType;
import org.springframework.stereotype.Component;

@Component
public class StraightNumberBetStrategy extends RouletteBetStrategySupport {

    @Override
    public BetType betType() {
        return BetType.STRAIGHT_NUMBER;
    }

    @Override
    public boolean isWinning(String betValue, int ballPosition) {
        validateBallPosition(ballPosition);

        int betNumber = parseBetNumber(betValue);

        if (betNumber < 0 || betNumber > 36) {
            throw new BadRouletteRequestException("STRAIGHT_NUMBER bet value must be between 0 and 36");
        }

        return betNumber == ballPosition;
    }

    @Override
    public int payoutMultiplier() {
        return 35;
    }

    @Override
    public int winningOutcomes() {
        return 1;
    }
}
