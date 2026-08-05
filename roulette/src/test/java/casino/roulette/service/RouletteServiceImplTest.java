package casino.roulette.service;

import casino.roulette.exceptions.RouletteGameNotFoundException;
import casino.roulette.game.RouletteEngine;
import casino.roulette.model.RouletteGameEntity;
import casino.roulette.repository.RouletteGameRepository;
import casino.roulette.request.RoulettePlayRequestDTO;
import casino.roulette.requestClients.banking.BankingRestClient;
import casino.roulette.util.BetType;
import casino.roulette.validation.RouletteRequestValidator;
import casino.roulette.view.RouletteGameDTO;
import casino.roulette.view.RoulettePlayResultDTO;
import casino.roulette.view.RouletteStatsDTO;
import casino.roulette.view.RouletteUserStatsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RouletteServiceImplTest {

    @Mock
    private RouletteGameRepository rouletteGameRepository;

    @Mock
    private RouletteEngine rouletteEngine;

    @Mock
    private BankingRestClient bankingRestClient;

    @Mock
    private RouletteStatsCalculator rouletteStatsCalculator;

    @Mock
    private RouletteInfoService rouletteInfoService;

    @Mock
    private RouletteRequestValidator rouletteRequestValidator;

    private RouletteServiceImpl rouletteService;

    @BeforeEach
    void setUp() {
        rouletteService = new RouletteServiceImpl(
                rouletteGameRepository,
                rouletteEngine,
                bankingRestClient,
                rouletteStatsCalculator,
                rouletteInfoService,
                rouletteRequestValidator
        );
    }

    @Test
    void playValidatesUserChecksBankingPlaysCreatesTransactionAndSavesGame() {
        RoulettePlayRequestDTO request = new RoulettePlayRequestDTO(
                1L,
                BetType.COLOR,
                "RED",
                new BigDecimal("10.00")
        );
        RouletteGameEntity game = game(1L, true, "10.00", "10.00");

        when(rouletteEngine.play(request)).thenReturn(game);
        when(rouletteGameRepository.save(game)).thenReturn(game);

        RoulettePlayResultDTO result = rouletteService.play(request);

        verify(rouletteRequestValidator).validatePlayRequest(request);
        verify(bankingRestClient).findUserById(1L);
        verify(rouletteEngine).play(request);
        verify(bankingRestClient).createRouletteTransaction(1L, new BigDecimal("10.00"));
        verify(rouletteGameRepository).save(game);
        assertNotNull(result);
        assertEquals(1L, result.getUser());
        assertEquals(BetType.COLOR, result.getBetType());
    }

    @Test
    void getRulesAndChancesDelegateToInfoService() {
        when(rouletteInfoService.getRules()).thenReturn("rules");
        when(rouletteInfoService.getChances()).thenReturn("chances");

        assertEquals("rules", rouletteService.getRules());
        assertEquals("chances", rouletteService.getChances());
    }

    @Test
    void getStatsDelegatesToCalculatorWithAllGames() {
        List<RouletteGameEntity> games = List.of(game(1L, true, "10.00", "10.00"));
        RouletteStatsDTO stats = new RouletteStatsDTO(
                1,
                1,
                BigDecimal.ZERO,
                new BigDecimal("10.00"),
                new BigDecimal("10.00")
        );

        when(rouletteGameRepository.findAll()).thenReturn(games);
        when(rouletteStatsCalculator.calculateStats(games)).thenReturn(stats);

        assertEquals(stats, rouletteService.getStats());
    }

    @Test
    void getUserStatsValidatesUserChecksBankingAndDelegatesToCalculator() {
        List<RouletteGameEntity> games = List.of(game(1L, true, "10.00", "10.00"));
        RouletteUserStatsDTO stats = new RouletteUserStatsDTO(
                1L,
                1,
                new BigDecimal("10.00"),
                BigDecimal.ZERO,
                new BigDecimal("10.00"),
                new BigDecimal("10.00"),
                new BigDecimal("-10.00")
        );

        when(rouletteGameRepository.findByUser(1L)).thenReturn(games);
        when(rouletteStatsCalculator.calculateUserStats(1L, games)).thenReturn(stats);

        assertEquals(stats, rouletteService.getUserStats(1L));
        verify(rouletteRequestValidator).validateUserId(1L);
        verify(bankingRestClient).findUserById(1L);
    }

    @Test
    void getGamesMapsAllEntitiesToDtos() {
        when(rouletteGameRepository.findAll()).thenReturn(List.of(
                game(1L, true, "10.00", "10.00"),
                game(2L, false, "-5.00", "5.00")
        ));

        List<RouletteGameDTO> result = rouletteService.getGames();

        assertEquals(2, result.size());
        assertEquals(1L, result.getFirst().getUser());
        assertEquals(2L, result.getLast().getUser());
    }

    @Test
    void getGameReturnsMappedGame() {
        RouletteGameEntity game = game(1L, true, "10.00", "10.00");

        when(rouletteGameRepository.findById(1L)).thenReturn(Optional.of(game));

        RouletteGameDTO result = rouletteService.getGame(1L);

        assertEquals(1L, result.getUser());
    }

    @Test
    void getGameThrowsWhenGameDoesNotExist() {
        when(rouletteGameRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RouletteGameNotFoundException.class, () -> rouletteService.getGame(99L));
    }

    @Test
    void deleteGameDeletesAndReturnsMappedGame() {
        RouletteGameEntity game = game(1L, true, "10.00", "10.00");

        when(rouletteGameRepository.findById(1L)).thenReturn(Optional.of(game));

        RouletteGameDTO result = rouletteService.deleteGame(1L);

        verify(rouletteGameRepository).delete(game);
        assertEquals(1L, result.getUser());
    }

    private RouletteGameEntity game(Long user, boolean winning, String amount, String betAmount) {
        return RouletteGameEntity.create(
                user,
                winning,
                new BigDecimal(amount),
                new BigDecimal(betAmount),
                BetType.COLOR,
                "RED",
                7
        );
    }
}
