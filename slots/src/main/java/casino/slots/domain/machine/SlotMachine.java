package casino.slots.domain.machine;

import casino.slots.domain.dto.GameResult;
import casino.slots.domain.dto.OutCome;
import casino.slots.domain.enums.Symbol;
import casino.slots.domain.ruleSet.Rule;
import casino.slots.model.SlotsGameEntity;
import casino.slots.request.SlotsPlayRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SlotMachine implements SlotEngine {

    CashOutMultiplier calculator;
    List<SlotField> fields = new ArrayList<>();
    Rule allRules;
    private final Map<Symbol, Integer> weights;
    private final int numberOfFields;

    public SlotMachine(CashOutMultiplier calculator, int numberOfFields, Map<Symbol, Integer> weights, Rule rules) {
        this.calculator = calculator;
        for(int i = 0; i < numberOfFields; i++) {
            fields.add(new SlotField(weights, new Random()));
        }
        this.weights = weights;
        this.numberOfFields = numberOfFields;
        this.allRules = rules;
    }


    public GameResult play(BigDecimal betAmount) {
        OutCome result = spin();
        BigDecimal amount =  calculateWinningAmount(result, betAmount);
        boolean isWinning = true;

        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
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


    @Override
    public SlotsGameEntity play(SlotsPlayRequest request) {
        SlotsGameEntity finishedGame = new SlotsGameEntity();

        BigDecimal betAmount = BigDecimal.valueOf(Long.parseLong(request.getBetAmount()));
        GameResult result = play(betAmount);

        finishedGame.setUserId(request.getUserId());
        finishedGame.setWinning(result.isWinning());
        finishedGame.setAmount(result.getAmount());
        finishedGame.setBetAmount(betAmount);
        finishedGame.setSlotStates(result.getSlotStates());

        return finishedGame;
    }
}
