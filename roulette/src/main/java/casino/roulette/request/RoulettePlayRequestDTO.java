package casino.roulette.request;

import casino.roulette.util.BetType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class RoulettePlayRequestDTO {
    private Long user;
    private BetType betType;
    private String betValue;
    private BigDecimal amount;
}
