package casino.roulette.game;

import casino.roulette.game.strategy.RouletteBetStrategy;
import casino.roulette.game.strategy.RouletteBetStrategyResolver;
import casino.roulette.model.RouletteGameEntity;
import casino.roulette.request.RoulettePlayRequestDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class RouletteEngine {

    private final RouletteSpinGenerator spinGenerator;
    private final RouletteBetStrategyResolver rouletteBetStrategyResolver;

    public RouletteEngine(
            RouletteSpinGenerator spinGenerator,
            RouletteBetStrategyResolver rouletteBetStrategyResolver
    ) {
        this.spinGenerator = spinGenerator;
        this.rouletteBetStrategyResolver = rouletteBetStrategyResolver;
    }

    public RouletteGameEntity play(RoulettePlayRequestDTO request) {
        int ballPosition = spinGenerator.spin();
        RouletteBetStrategy strategy = rouletteBetStrategyResolver.resolve(request.getBetType());

        boolean winning = strategy.isWinning(request.getBetValue(), ballPosition);

        BigDecimal amount = calculateAmount(request, winning, strategy);

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

    private BigDecimal calculateAmount(
            RoulettePlayRequestDTO request,
            boolean winning,
            RouletteBetStrategy strategy
    ) {
        if (!winning) {
            return request.getAmount().negate();
        }

        return request.getAmount().multiply(BigDecimal.valueOf(strategy.payoutMultiplier()));
    }
}
