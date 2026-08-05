package casino.banking.util;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoneyHelperTest {

    private static long SUITE_SEED;
    private Random random;

    @BeforeAll
    static void initSuiteSeed(){

        String raw = System.getProperty("test.seed");

        SUITE_SEED = (raw == null || raw.isBlank())
                ? System.nanoTime()
                : Long.parseLong(raw);
    }

    @BeforeEach
    void initRandom(TestInfo testInfo) {

        long testSeed = SUITE_SEED ^ testInfo.getDisplayName().hashCode();
        random = new Random(testSeed);
    }

    @AfterAll
    static void printSeed(){

        System.out.println("Seed used: " + SUITE_SEED);
    }

    @Test
    void createBigDecimal_validNumberTwoDigitDecimal_2Decimals_returnCorrect() {
        BigInteger number = BigInteger.valueOf(20L);
        int decimals = 10;
        BigDecimal result = MoneyHelper.createBigDecimal2Decimals(number, decimals);
        assertTrue(new BigDecimal("20.10").equals(result));
    }

    @Test
    void createBigDecimal_validNumberOneDigitDecimal_2Decimals_returnCorrect() {
        BigInteger number = BigInteger.valueOf(20L);
        int decimals = 2;
        BigDecimal result = MoneyHelper.createBigDecimal2Decimals(number, decimals);
        assertTrue(new BigDecimal("20.02").equals(result));
    }

    @Test
    void createBigDecimal_2Decimals_minDecimals_returnCorrect() {
        BigInteger number = BigInteger.valueOf(10L);
        int decimals = 0;
        BigDecimal result = MoneyHelper.createBigDecimal2Decimals(number, decimals);
        assertEquals(new BigDecimal("10.00"), result);
    }

    @Test
    void extractFractionPart2Decimals_extractFraction() {
        BigDecimal number = new BigDecimal("20.33");

        int result = MoneyHelper.extractFractionPart2Decimals(number);

        assertEquals(33, result);
    }

    @Test
    void extractIntegerPart_extractsIntegerPart() {
        BigDecimal number = new BigDecimal("20.33");

        BigInteger result =MoneyHelper.extractIntegerPart(number);

        assertEquals(new BigInteger("20"), result);
    }

}
