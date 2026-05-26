package casino.banking.util;

import java.math.BigDecimal;

public class MoneyHelper {
    public static  BigDecimal createBigDecimal(BigDecimal amount, int decimals) {
        String asString = amount.toString() + '.' + String.format("%02d", decimals);;
        return new BigDecimal(asString);
    }
}
