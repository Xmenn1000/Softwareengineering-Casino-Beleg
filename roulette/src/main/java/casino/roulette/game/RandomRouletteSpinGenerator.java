package casino.roulette.game;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class RandomRouletteSpinGenerator implements RouletteSpinGenerator {

    @Override
    public int spin() {
        return ThreadLocalRandom.current().nextInt(37);
    }
}
