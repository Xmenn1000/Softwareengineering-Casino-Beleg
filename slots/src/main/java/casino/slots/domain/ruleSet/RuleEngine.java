package casino.slots.domain.ruleSet;

import casino.slots.domain.dto.OutCome;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RuleEngine implements Rule {

    private final List<Rule> rules;

    public RuleEngine(List<Rule> rules) {
        this.rules = rules;

    }

    // take highest win
    @Override
    public BigDecimal payOut(OutCome symbols, BigDecimal amount) {
        BigDecimal highestWin = BigDecimal.valueOf(0);
        for(Rule r : rules) {
            BigDecimal subresult = r.payOut(symbols, amount);
            if(subresult.compareTo(highestWin) > 0) {
                highestWin = subresult;
            }
        }
        return highestWin;
    }
}
