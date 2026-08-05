package casino.slots;

import casino.slots.exeptions.BankingUserNotFoundException;
import casino.slots.model.SlotsGameEntity;
import casino.slots.repository.SlotsGameRepository;
import casino.slots.restClient.BankingRestClient;
import casino.slots.service.SlotsStatsCalculator;
import casino.slots.service.StatsServiceImpl;
import casino.slots.view.SlotsStatsDTO;
import casino.slots.view.SlotsStatsUserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatsServiceImplTest {

    @Mock
    private SlotsGameRepository repository;

    @Mock
    private SlotsStatsCalculator calculator;

    @Mock
    private BankingRestClient bankingRestClient;

    private StatsServiceImpl statsService;

    @BeforeEach
    void setUp() {
        statsService = new StatsServiceImpl(
                repository,
                calculator,
                bankingRestClient
        );
    }

    @Test
    void shouldCreateOverallStats() {
        List<SlotsGameEntity> games = List.of(
                new SlotsGameEntity(),
                new SlotsGameEntity(),
                new SlotsGameEntity()
        );

        when(repository.findAll()).thenReturn(games);

        when(calculator.totalClientCount(games))
                .thenReturn(2L);

        when(calculator.totalGamesCount(games))
                .thenReturn(3L);

        when(calculator.totalProfit(games))
                .thenReturn(new BigDecimal("-5.00"));

        when(calculator.totalCashOut(games))
                .thenReturn(new BigDecimal("20.00"));

        when(calculator.totalTurnover(games))
                .thenReturn(new BigDecimal("25.00"));

        SlotsStatsDTO result = statsService.getStats();

        assertEquals(2L, result.getTotal_client_count());
        assertEquals(3L, result.getTotal_games_count());

        assertBigDecimalEquals(
                "−5.00".replace("−", "-"),
                result.getTotal_profit()
        );

        assertBigDecimalEquals(
                "20.00",
                result.getTotal_cashout()
        );

        assertBigDecimalEquals(
                "25.00",
                result.getTotal_turnover()
        );

        verify(repository).findAll();
        verify(calculator).totalClientCount(games);
        verify(calculator).totalGamesCount(games);
        verify(calculator).totalProfit(games);
        verify(calculator).totalCashOut(games);
        verify(calculator).totalTurnover(games);

        verifyNoInteractions(bankingRestClient);
    }

    @Test
    void shouldCreateStatsForSpecificUser() {
        Long userId = 5L;

        List<SlotsGameEntity> userGames = List.of(
                new SlotsGameEntity(),
                new SlotsGameEntity()
        );

        when(repository.findByUserId(userId))
                .thenReturn(userGames);

        when(calculator.totalGamesCount(userGames))
                .thenReturn(2L);

        when(calculator.totalWinnings(userGames))
                .thenReturn(new BigDecimal("30.00"));

        when(calculator.totalLosses(userGames))
                .thenReturn(new BigDecimal("10.00"));

        when(calculator.totalClientProfit(userGames))
                .thenReturn(new BigDecimal("20.00"));

        when(calculator.totalHouseTurnoverFromClient(userGames))
                .thenReturn(new BigDecimal("20.00"));

        when(calculator.totalHouseProfitFromClient(userGames))
                .thenReturn(new BigDecimal("-20.00"));

        SlotsStatsUserDTO result =
                statsService.getStatsByUserId(userId);

        assertEquals(5L, result.getClient());
        assertEquals(2L, result.getTotal_games_count());

        assertBigDecimalEquals(
                "30.00",
                result.getTotal_winnings()
        );

        assertBigDecimalEquals(
                "10.00",
                result.getTotal_losses()
        );

        assertBigDecimalEquals(
                "20.00",
                result.getTotal_client_profit()
        );

        assertBigDecimalEquals(
                "20.00",
                result.getTotal_house_turnover_from_client()
        );

        assertBigDecimalEquals(
                "-20.00",
                result.getTotal_house_profit_from_client()
        );

        verify(bankingRestClient).findUserById(userId);
        verify(repository).findByUserId(userId);
    }

    @Test
    void shouldStopWhenBankingUserDoesNotExist() {
        Long userId = 99L;

        doThrow(new BankingUserNotFoundException(userId))
                .when(bankingRestClient)
                .findUserById(userId);

        assertThrows(
                BankingUserNotFoundException.class,
                () -> statsService.getStatsByUserId(userId)
        );

        verify(bankingRestClient).findUserById(userId);

        verify(repository, never())
                .findByUserId(anyLong());

        verifyNoInteractions(calculator);
    }

    private void assertBigDecimalEquals(
            String expected,
            BigDecimal actual
    ) {
        assertEquals(
                0,
                new BigDecimal(expected).compareTo(actual)
        );
    }
}