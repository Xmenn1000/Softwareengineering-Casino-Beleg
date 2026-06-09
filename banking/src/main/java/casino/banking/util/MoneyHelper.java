package casino.banking.util;

import java.math.BigDecimal;
import java.math.BigInteger;

public class MoneyHelper {
    public static BigDecimal createBigDecimal2Decimals(BigInteger amount, int decimals) {
        return new BigDecimal(amount).add(BigDecimal.valueOf(decimals, 2));
    }

    // https://stackoverflow.com/questions/10038749/java-extract-just-the-fractional-part-of-a-bigdecimal
    public static int extractFractionPart2Decimals(BigDecimal amount) {
        return amount.remainder(BigDecimal.ONE).movePointRight(2).abs().intValueExact();
    }

    public static BigInteger extractIntegerPart(BigDecimal amount) {
        return amount.toBigInteger();
    }
}
