package casino.roulette.game.strategy;

import casino.roulette.exceptions.BadRouletteRequestException;
import casino.roulette.util.BetType;
import org.springframework.stereotype.Component;

@Component
public class ParityBetStrategy extends RouletteBetStrategySupport {

    @Override
    public BetType betType() {
        return BetType.PARITY;
    }

    @Override
    public boolean isWinning(String betValue, int ballPosition) {
        validateBallPosition(ballPosition);

        if (ballPosition == 0) {
            return false;
        }

        String normalizedBetValue = normalize(betValue);

        if (!normalizedBetValue.equals("EVEN") && !normalizedBetValue.equals("ODD")) {
            throw new BadRouletteRequestException("PARITY bet value must be EVEN or ODD");
        }

        boolean ballIsEven = ballPosition % 2 == 0;

        return normalizedBetValue.equals("EVEN") == ballIsEven;
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
