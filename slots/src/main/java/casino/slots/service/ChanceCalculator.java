package casino.slots.service;

import casino.slots.domain.config.SlotPropertiesConfig;
import casino.slots.domain.enums.Symbol;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChanceCalculator {

    private final SlotPropertiesConfig config;

    public ChanceCalculator(SlotPropertiesConfig config) {
        this.config = config;
    }

    // Rechnet für jedes Symbol die Chance aus, auf einer Walze zu erscheinen:
    // Gewicht des Symbols geteilt durch die Summe aller Gewichte.
    public Map<Symbol, Double> calculateChancesPerSymbol() {
        int totalChances = config.getWeights().values().stream().mapToInt(Integer::intValue).sum();
        Map<Symbol, Double> chancesPerSymbol = new HashMap<>();
        for (Map.Entry<Symbol, Integer> e : config.getWeights().entrySet()) {
            double chance = (double) e.getValue() / totalChances;
            chancesPerSymbol.put(e.getKey(), chance);
        }
        return chancesPerSymbol;
    }

    // Berechnet die Wahrscheinlichkeit, dass ein Symbol genau `howManyMatches` mal auf den Walzen erscheint.
    // Jede Walze ist ein eigener Versuch mit Chance chanceForSymbol, das Symbol zu zeigen (Binomialverteilung).
    // Formel: C(reels, matches) * p^matches * (1-p)^(reels-matches).
    // C(reels, matches) zählt, auf welchen Walzen die Treffer liegen können.
    // https://en.wikipedia.org/wiki/Binomial_distribution
    // https://mathworld.wolfram.com/BinomialDistribution.html
    public double comboChance(double chanceForSymbol, int howManyMatches) {
        int reels = config.getNumberOfFields();
        return binomial(reels, howManyMatches)
                * Math.pow(chanceForSymbol, howManyMatches)
                * Math.pow(1 - chanceForSymbol, reels - howManyMatches);
    }

    // Berechnet den Binomialkoeffizienten C(n, k): auf wie viele Arten man k Dinge aus n auswählen kann.
    // Statt der Fakultät-Formel n! / (k! * (n-k)!) (läuft schnell über) wird Schritt für Schritt
    // multipliziert: C(n,k) = n/1 * (n-1)/2 * (n-2)/3 * ... * (n-k+1)/k.
    // https://de.wikipedia.org/wiki/Binomialkoeffizient
    // https://mathworld.wolfram.com/BinomialCoefficient.html
    private long binomial(int n, int k) {
        long result = 1;
        for (int i = 0; i < k; i++) {
            result = result * (n - i) / (i + 1);
        }
        return result;
    }
}
