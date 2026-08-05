package casino.roulette.game.strategy;

import casino.roulette.exceptions.BadRouletteRequestException;
import casino.roulette.util.BetType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class RouletteBetStrategyResolver {

    private final Map<BetType, RouletteBetStrategy> strategies = new EnumMap<>(BetType.class);

    public RouletteBetStrategyResolver(List<RouletteBetStrategy> strategies) {
        for (RouletteBetStrategy strategy : strategies) {
            this.strategies.put(strategy.betType(), strategy);
        }
    }

    public RouletteBetStrategy resolve(BetType betType) {
        RouletteBetStrategy strategy = strategies.get(betType);

        if (strategy == null) {
            throw new BadRouletteRequestException("Bet type is not supported yet: " + betType);
        }

        return strategy;
    }
}
