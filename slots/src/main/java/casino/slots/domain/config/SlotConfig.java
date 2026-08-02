package casino.slots.domain.config;

import casino.slots.domain.machine.CashOutMultiplier;
import casino.slots.domain.machine.SlotEngine;
import casino.slots.domain.machine.SlotMachine;
import casino.slots.domain.enums.ResultPattern;
import casino.slots.domain.ruleSet.RuleEngine;
import casino.slots.domain.ruleSet.ExactCountRule;
import casino.slots.domain.ruleSet.Rule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SlotConfig {

    @Bean
    public CashOutMultiplier cashOutMultiplier(SlotPropertiesConfig slotProperties) {
        return new CashOutMultiplier(slotProperties.getPayout());
    }

    @Bean
    public Rule ruleEngine(CashOutMultiplier cashOutMultiplier) {

        List<Rule> rules = List.of(
                new ExactCountRule(1, ResultPattern.ONE_OF_A_KIND, cashOutMultiplier),
                new ExactCountRule(2, ResultPattern.TWO_OF_A_KIND, cashOutMultiplier),
                new ExactCountRule(3, ResultPattern.THREE_OF_A_KIND, cashOutMultiplier));

        return new RuleEngine(rules);
    }

    @Bean
    public SlotEngine slotMachine(CashOutMultiplier cashOutMultiplier, SlotPropertiesConfig slotProperties, Rule rules) {
        return new SlotMachine(cashOutMultiplier, slotProperties.getNumberOfFields(), slotProperties.getWeights(), rules);
    }

}
