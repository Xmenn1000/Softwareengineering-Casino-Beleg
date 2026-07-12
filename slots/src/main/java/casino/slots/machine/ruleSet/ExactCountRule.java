package casino.slots.machine.ruleSet;

import casino.slots.machine.CashOutMultiplier;
import casino.slots.machine.OutCome;
import casino.slots.machine.enums.ResultPattern;
import casino.slots.machine.enums.Symbol;

import java.math.BigDecimal;
import java.util.EnumSet;

public class ExactCountRule implements Rule {

    private final int exactCount;
    private final ResultPattern pattern;
    private CashOutMultiplier calculator;

    public ExactCountRule(int exactCount, ResultPattern pattern, CashOutMultiplier calculator) {
        this.calculator = calculator;
        this.exactCount = exactCount;
        this.pattern = pattern;
    }

    @Override
    public BigDecimal payOut(OutCome result, BigDecimal amount) {
        BigDecimal payOut = BigDecimal.valueOf(0);
        for(Symbol s : EnumSet.allOf(Symbol.class)) {
            if(result.numberOfSymbols(s) == exactCount) {
                BigDecimal newAmount = amount.multiply(calculator.multiplier(pattern, s));
                payOut = payOut.add(newAmount);
            }
        }
        return payOut;
    }
}
