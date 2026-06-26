package casino.roulette.view;

import casino.roulette.util.BetType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class RoulettePlayResultDTO {
    private Long user;
    private boolean winning;
    private BigDecimal amount;
    private BigDecimal betAmount;
    private BetType betType;
    private String betValue;
    private int ballPosition;
}
