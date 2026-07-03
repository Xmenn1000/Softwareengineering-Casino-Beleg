package casino.roulette.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "casino.roulette")
public class RouletteProperties {

    private Betting betting = new Betting();
    private Banking banking = new Banking();

    @Getter
    @Setter
    public static class Betting {
        private BigDecimal minAmount = BigDecimal.ONE;
        private BigDecimal maxAmount = new BigDecimal("1000.00");
    }

    @Getter
    @Setter
    public static class Banking {
        private String invoicingParty = "ROULETTE";
    }
}
