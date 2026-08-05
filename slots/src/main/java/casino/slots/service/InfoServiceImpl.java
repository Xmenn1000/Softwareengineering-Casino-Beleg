package casino.slots.service;

import casino.slots.domain.config.SlotPropertiesConfig;
import casino.slots.domain.enums.ResultPattern;
import casino.slots.domain.enums.Symbol;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InfoServiceImpl implements InfoService {

    private final SlotPropertiesConfig config;
    private final ChanceCalculator chanceCalculator;

    public InfoServiceImpl(SlotPropertiesConfig config, ChanceCalculator chanceCalculator) {
        this.config = config;
        this.chanceCalculator = chanceCalculator;
    }

    @Override
    public String getRules() {
        return """
            Slots - Game Rules
            How can I lose my money playing Slots? Good question. Here are the rules.

            The goal:
            Pull the lever, spin 3 reels, and pray to the SEVENs. That's it. That's the strategy.

            How the magic happens:
            - Pick a bet amount. This is the money you are about to say goodbye to.
            - Hit spin. Each of the 3 reels lands on a random symbol.
            - From "meh" to "call your family": CHERRY, LEMON, ORANGE, PLUM, GOLDBAR, SEVEN.

            When you actually win (it happens, occasionally):
            - Only your best matching combo counts. We are generous like that. Once. I Mean the House always wins right? right!
            - Three of a kind is the jackpot dream. Chase it responsibly. Or don't.
            - GOLDBAR and SEVEN are so fancy that even two of them pay out, But dont count on them. 
              and a lonely single SEVEN still tips you a little.
            - Want the cold, hard numbers? They live over at /chances.

            The fine print (aka how the money leaves):
            - Every spin quietly subtracts your bet from your balance.
            - Win, and you get your bet back times a multiplier. Cha-ching.
            - Lose, and... well. The bet is gone. 
            Always Rember the House Always wins. :) 
            """;
    }

    @Override
    public String getChances() {
        StringBuilder sBuilder = new StringBuilder();
        Map<Symbol, Double> reelChances = chanceCalculator.calculateChancesPerSymbol();
        Map<ResultPattern, Map<Symbol, Integer>> payout = config.getPayout();

        sBuilder.append("Slots - Win Chances\n");
        sBuilder.append("Each spin turns ").append(config.getNumberOfFields()).append(" reels. Only the best matching combo of Symbols counts. \n");

        sBuilder.append(getSymbolChancesString(reelChances));

        sBuilder.append("\nWinning combinations:\n");
        sBuilder.append(String.format("  %-14s %10s   %-8s %s\n",
                "Combo", "Chance", "Payout", "(win = bet x mult)"));

        sBuilder.append(getPayoutString(reelChances, payout));

        sBuilder.append("\nHow your profit is calculated:\n");
        sBuilder.append("  win  = bet * multiplier   (best matching combo only)\n");
        sBuilder.append("  loss = -bet\n");

        return sBuilder.toString();
    }

    // Baut den Text-Block mit allen Symbolen und ihrer Chance pro Walze (absteigend sortiert).
    private String getSymbolChancesString(Map<Symbol, Double> reelChances) {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("Symbols and Chances to appear in each spin\n");

        // https://sentry.io/answers/how-to-sort-a-map-key-value-by-values-in-java/
        Map<Symbol, Double> chancesSorted = reelChances.entrySet().stream()
                .sorted(Map.Entry.<Symbol, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
        for (Map.Entry<Symbol, Double> e : chancesSorted.entrySet()) {
            sBuilder.append(String.format("%s: %.2f\n", e.getKey(), e.getValue()));
        }

        return sBuilder.toString();
    }

    private int patternCount(ResultPattern pattern) {
        return switch(pattern) {
            case THREE_OF_A_KIND -> 3;
            case TWO_OF_A_KIND -> 2;
            case ONE_OF_A_KIND -> 1;
        };
    }

    private String getPayoutString(Map<Symbol, Double> reelChances, Map<ResultPattern, Map<Symbol, Integer>> payout) {
        StringBuilder sBuilder = new StringBuilder();

        for (ResultPattern pattern : List.of(
                ResultPattern.THREE_OF_A_KIND,
                ResultPattern.TWO_OF_A_KIND,
                ResultPattern.ONE_OF_A_KIND)) {

            Map<Symbol, Integer> multipliers = payout.get(pattern);
            if (multipliers == null) continue;
            int count = patternCount(pattern);

            multipliers.entrySet().stream()
                    .filter(e -> e.getValue() > 0)
                    .sorted(Map.Entry.<Symbol, Integer>comparingByValue().reversed())
                    .forEach(e -> {
                        double chance = chanceCalculator.comboChance(reelChances.get(e.getKey()), count);
                        String combo = count + "x " + e.getKey();
                        sBuilder.append(String.format("  %-14s %9.4f%%   x%-7d bet x %d\n",
                                combo, chance * 100, e.getValue(), e.getValue()));
                    });
        }

        return sBuilder.toString();

    }
}
