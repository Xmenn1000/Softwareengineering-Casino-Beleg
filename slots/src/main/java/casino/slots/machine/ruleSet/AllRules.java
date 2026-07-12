package casino.slots.machine.ruleSet;

import casino.slots.machine.OutCome;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AllRules implements Rule {

    private List<Rule> allRules = new ArrayList<>();

    public void addRule(Rule newRule) {
        allRules.add(newRule);
    }

    public void removeRule(Rule ruleToRemove) {
        allRules.remove(ruleToRemove);
    }


    @Override
    public BigDecimal payOut(OutCome symbols, BigDecimal amount) {
        BigDecimal result = BigDecimal.valueOf(0);
        for(Rule r : allRules) {
            result = result.add(r.payOut(symbols, amount));
        }

        return result;
    }
}
