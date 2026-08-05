package casino.banking.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class MoneyHelper {

    public static BigDecimal createBigDecimal2Decimals(BigInteger amount, int decimals) {
        return new BigDecimal(amount).add(BigDecimal.valueOf(decimals, 2));
    }

    // https://stackoverflow.com/questions/10038749/java-extract-just-the-fractional-part-of-a-bigdecimal
    public static int extractFractionPart2Decimals(BigDecimal amount) {
        BigDecimal floored = amount.setScale(0, RoundingMode.FLOOR);
        return amount.subtract(floored).movePointRight(2).intValueExact();
    }

    public static BigInteger extractIntegerPart(BigDecimal amount) {
        return amount.setScale(0, RoundingMode.FLOOR).toBigInteger();
    }
}
