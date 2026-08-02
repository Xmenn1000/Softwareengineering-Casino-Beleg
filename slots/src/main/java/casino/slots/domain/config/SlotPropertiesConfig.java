package casino.slots.domain.config;

import casino.slots.domain.enums.ResultPattern;
import casino.slots.domain.enums.Symbol;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

//https://stackoverflow.com/questions/51997377/configurationproperties-is-not-working-with-yaml-files
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "slots.machine")
@Getter
@Setter
public class SlotPropertiesConfig {
    private Map<Symbol, Integer> weights;
    private Map<ResultPattern, Map<Symbol, Integer>> payout;
    private String rules;
    private int numberOfFields;
}
