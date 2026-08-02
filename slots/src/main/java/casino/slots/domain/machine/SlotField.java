package casino.slots.domain.machine;

import casino.slots.domain.enums.Symbol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SlotField {

    private final List<Symbol> symbols = new ArrayList<>();
    private Map<Symbol, Integer> weights;
    private Random random;


    public SlotField(Map<Symbol, Integer> weights, Random random) {
        for(Map.Entry<Symbol, Integer> entry : weights.entrySet()) {
            for(int i = 0; i < entry.getValue(); i++) {
                this.symbols.add(entry.getKey());
            }
        }
        this.weights = weights;
        this.random = random;

    }

    public Symbol spin() {
        return symbols.get(random.nextInt(symbols.size()));
    }
}
