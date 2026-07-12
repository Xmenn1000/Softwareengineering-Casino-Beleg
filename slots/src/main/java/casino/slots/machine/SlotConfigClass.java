package casino.slots.machine;

import casino.slots.machine.enums.Symbol;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

public class SlotConfigClass {

    private final Map<Symbol, Integer> weights;

    public SlotConfigClass() {
        Map<Symbol, Integer> w = new EnumMap<>(Symbol.class);
        w.put(Symbol.CHERRY,  8);
        w.put(Symbol.LEMON,   5);
        w.put(Symbol.ORANGE,  3);
        w.put(Symbol.PLUM,    2);
        w.put(Symbol.GOLDBAR, 1);
        w.put(Symbol.SEVEN,   1);
        this.weights = Collections.unmodifiableMap(w);
    }

    public Map<Symbol, Integer> getWeights() {
        return weights;
    }

    public int totalWeight() {
        return weights.values().stream().mapToInt(Integer::intValue).sum();
    }
}
