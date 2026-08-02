package casino.slots.domain.machine;

import casino.slots.domain.enums.ResultPattern;
import casino.slots.domain.enums.Symbol;

import java.math.BigDecimal;
import java.util.Map;

public class CashOutMultiplier {

    private final Map<ResultPattern, Map<Symbol, Integer>> patternMultiplierMap;

    public CashOutMultiplier(Map<ResultPattern, Map<Symbol, Integer>> patternMultiplierMap) {
        this.patternMultiplierMap = patternMultiplierMap;
    }


    public BigDecimal getMultiplierForPattern(ResultPattern pattern, Symbol symbol) {

        Map<Symbol, Integer> symbolMap = patternMultiplierMap.get(pattern);

        return BigDecimal.valueOf(symbolMap.getOrDefault(symbol, 0));
    }
}
