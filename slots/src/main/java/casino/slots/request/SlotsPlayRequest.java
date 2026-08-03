package casino.slots.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class SlotsPlayRequest {
    private Long userId;
    private BigDecimal amount;
    private String betAmount;
}


