package casino.slots.machine;

import casino.slots.machine.enums.ResultPattern;
import casino.slots.machine.enums.Symbol;
import casino.slots.machine.ruleSet.AllRules;
import casino.slots.machine.ruleSet.ExactCountRule;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class SlotMachine {

    CashOutMultiplier calculator;
    List<SlotField> fields = new ArrayList<>();
    AllRules allRules = new AllRules();
    private final int numberOfFields = 3;
    private final SlotConfigClass weightsProvider = new SlotConfigClass();

    public SlotMachine(CashOutMultiplier calculator) {
        this.calculator = calculator;
        for(int i = 0; i < numberOfFields; i++) {
            fields.add(new SlotField(weightsProvider.getWeights(), new Random()));
        }
        allRules.addRule(new ExactCountRule(1, ResultPattern.ONE_OF_A_KIND, calculator));
        allRules.addRule(new ExactCountRule(2, ResultPattern.TWO_OF_A_KIND, calculator));
        allRules.addRule(new ExactCountRule(3, ResultPattern.THREE_OF_A_KIND, calculator));
    }


    public GameResult play(BigDecimal betAmount) {
        OutCome result = spin();
        BigDecimal amount =  calculateWinningAmount(result, betAmount);
        boolean isWinning = true;


        if(amount.compareTo(BigDecimal.ZERO) == 0) {
            isWinning = false;
            amount = BigDecimal.ZERO.subtract(betAmount);
        }

        return new GameResult(isWinning, amount, result.getOutCome());
    }

    public OutCome spin() {
        List<Symbol> result = new ArrayList<>();
        for(SlotField f : fields) {
            result.add(f.spin());
        }
        return new OutCome(result);
    }

    public BigDecimal calculateWinningAmount(OutCome outcome, BigDecimal betAmount) {
        return allRules.payOut(outcome, betAmount);
    }


}
