package casino.roulette.game;

import casino.roulette.exceptions.BadRouletteRequestException;
import casino.roulette.model.RouletteGameEntity;
import casino.roulette.request.RoulettePlayRequestDTO;
import casino.roulette.util.RouletteRules;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class RouletteEngine {

    private final RouletteSpinGenerator spinGenerator;

    public RouletteEngine(RouletteSpinGenerator spinGenerator) {
        this.spinGenerator = spinGenerator;
    }

    public RouletteGameEntity play(RoulettePlayRequestDTO request) {
        validateRequest(request);

        int ballPosition = spinGenerator.spin();

        boolean winning = RouletteRules.isWinningBet(
                request.getBetType(),
                request.getBetValue(),
                ballPosition
        );

        BigDecimal amount = calculateAmount(request, winning);

        return RouletteGameEntity.create(
                request.getUser(),
                winning,
                amount,
                request.getAmount(),
                request.getBetType(),
                request.getBetValue(),
                ballPosition
        );
    }

    private BigDecimal calculateAmount(RoulettePlayRequestDTO request, boolean winning) {
        if (!winning) {
            return request.getAmount().negate();
        }

        int multiplier = RouletteRules.payoutMultiplier(request.getBetType());
        return request.getAmount().multiply(BigDecimal.valueOf(multiplier));
    }

    private void validateRequest(RoulettePlayRequestDTO request) {
        if (request == null) {
            throw new BadRouletteRequestException("Request body must not be empty");
        }

        if (request.getUser() == null) {
            throw new BadRouletteRequestException("User must not be empty");
        }

        if (request.getBetType() == null) {
            throw new BadRouletteRequestException("Bet type must not be empty");
        }

        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRouletteRequestException("Amount must be greater than 0");
        }
    }
}
