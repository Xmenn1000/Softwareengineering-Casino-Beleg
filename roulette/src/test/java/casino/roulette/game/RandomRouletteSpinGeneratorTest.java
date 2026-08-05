package casino.roulette.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomRouletteSpinGeneratorTest {

    @Test
    void spinReturnsEuropeanRoulettePosition() {
        RandomRouletteSpinGenerator spinGenerator = new RandomRouletteSpinGenerator();

        for (int i = 0; i < 100; i++) {
            int result = spinGenerator.spin();

            assertTrue(result >= 0 && result <= 36);
        }
    }
}
