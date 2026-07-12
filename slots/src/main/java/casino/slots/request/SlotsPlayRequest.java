package casino.slots.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class SlotsPlayRequest {
    Long userId;
    BigDecimal amount;
}
