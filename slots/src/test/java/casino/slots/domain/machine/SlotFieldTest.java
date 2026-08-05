package casino.slots.domain.machine;

import casino.slots.domain.enums.Symbol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SlotFieldTest {

    @Mock
    private Random random;

    private Map<Symbol, Integer> weights;

    @BeforeEach
    void setUp() {
        weights = new LinkedHashMap<>();
        weights.put(Symbol.CHERRY, 2);
        weights.put(Symbol.LEMON, 1);
    }

    @Test
    void shouldReturnCherryForFirstWeightedIndex() {
        SlotField slotField = new SlotField(weights, random);

        when(random.nextInt(3)).thenReturn(0);

        Symbol result = slotField.spin();

        assertEquals(Symbol.CHERRY, result);
        verify(random).nextInt(3);
    }

    @Test
    void shouldReturnCherryForSecondWeightedIndex() {
        SlotField slotField = new SlotField(weights, random);

        when(random.nextInt(3)).thenReturn(1);

        Symbol result = slotField.spin();

        assertEquals(Symbol.CHERRY, result);
        verify(random).nextInt(3);
    }

    @Test
    void shouldReturnLemonForThirdWeightedIndex() {
        SlotField slotField = new SlotField(weights, random);

        when(random.nextInt(3)).thenReturn(2);

        Symbol result = slotField.spin();

        assertEquals(Symbol.LEMON, result);
        verify(random).nextInt(3);
    }

    @Test
    void shouldUseSumOfWeightsAsRandomBound() {
        Map<Symbol, Integer> customWeights = new LinkedHashMap<>();
        customWeights.put(Symbol.CHERRY, 2);
        customWeights.put(Symbol.LEMON, 3);

        SlotField slotField = new SlotField(customWeights, random);

        when(random.nextInt(5)).thenReturn(4);

        Symbol result = slotField.spin();

        assertEquals(Symbol.LEMON, result);
        verify(random).nextInt(5);
    }
}