package casino.slots.machine;

import casino.slots.machine.enums.ResultPattern;
import casino.slots.machine.enums.Symbol;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

@Service
public class CashOutMultiplier {

    private final Map<ResultPattern, Map<Symbol, Integer>> patternMultiplierMap;


    public CashOutMultiplier() {
        this.patternMultiplierMap = loadRules();
    }


    private Map<ResultPattern, Map<Symbol, Integer>> loadRules() {
        Map<Symbol, Integer> threeOfAKind = new EnumMap<>(Symbol.class);
        threeOfAKind.put(Symbol.CHERRY, 8);
        threeOfAKind.put(Symbol.LEMON, 25);
        threeOfAKind.put(Symbol.ORANGE, 40);
        threeOfAKind.put(Symbol.PLUM, 50);
        threeOfAKind.put(Symbol.GOLDBAR, 600);
        threeOfAKind.put(Symbol.SEVEN, 1500);

        Map<Symbol, Integer> twoOfAKind = new EnumMap<>(Symbol.class);
        twoOfAKind.put(Symbol.GOLDBAR, 8);
        twoOfAKind.put(Symbol.SEVEN, 10);

        Map<Symbol, Integer> oneOfAKind = new EnumMap<>(Symbol.class);
        oneOfAKind.put(Symbol.SEVEN, 2);

        Map<ResultPattern, Map<Symbol, Integer>> rules = new EnumMap<>(ResultPattern.class);
        rules.put(ResultPattern.THREE_OF_A_KIND, Collections.unmodifiableMap(threeOfAKind));
        rules.put(ResultPattern.TWO_OF_A_KIND, Collections.unmodifiableMap(twoOfAKind));
        rules.put(ResultPattern.ONE_OF_A_KIND, Collections.unmodifiableMap(oneOfAKind));

        rules.values().forEach(m -> {
            if (m.values().stream().anyMatch(mult -> mult <= 1))
                throw new IllegalStateException("multipliers must be > 1");
        });
        return Collections.unmodifiableMap(rules);
    }


    public BigDecimal multiplier(ResultPattern pattern, Symbol symbol) {

        Map<Symbol, Integer> symbolMap = patternMultiplierMap.get(pattern);

        return BigDecimal.valueOf(symbolMap.getOrDefault(symbol, 0));
    }
}
