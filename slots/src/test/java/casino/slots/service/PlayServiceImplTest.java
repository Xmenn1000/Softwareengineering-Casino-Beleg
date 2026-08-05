package casino.slots.service;

import casino.slots.domain.dto.GameResult;
import casino.slots.domain.enums.Symbol;
import casino.slots.domain.machine.SlotEngine;
import casino.slots.exeptions.BadSlotsRequestException;
import casino.slots.model.SlotsGameEntity;
import casino.slots.repository.SlotsGameRepository;
import casino.slots.request.SlotsPlayRequest;
import casino.slots.restClient.BankingRestClient;
import casino.slots.validation.SlotsGameEntityValidator;
import casino.slots.validation.SlotsRequestValidation;
import casino.slots.view.SlotsGameResultDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import casino.slots.exeptions.BankingUserNotFoundException;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayServiceImplTest {

    @Mock
    private SlotsGameRepository repository;

    @Mock
    private SlotsRequestValidation requestValidation;

    @Mock
    private SlotsGameEntityValidator entityValidator;

    @Mock
    private BankingRestClient bankingRestClient;

    @Mock
    private SlotEngine slotEngine;

    private PlayServiceImpl playService;

    @BeforeEach
    void setUp() {

        playService = new PlayServiceImpl(
                repository,
                requestValidation,
                entityValidator,
                bankingRestClient,
                slotEngine
        );
    }

    @Test
    void shouldPlayCreateTransactionAndSaveGame() {
        SlotsPlayRequest request = createRequest();

        List<Symbol> symbols = List.of(
                Symbol.SEVEN,
                Symbol.SEVEN,
                Symbol.SEVEN
        );

        GameResult gameResult = new GameResult(
                true,
                new BigDecimal("50.00"),
                symbols
        );

        when(slotEngine.play(request.getBetAmount()))
                .thenReturn(gameResult);

        when(repository.save(any(SlotsGameEntity.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        SlotsGameResultDTO result =
                playService.play(request);

        assertEquals(1L, result.getUserId());
        assertTrue(result.isWinning());

        assertEquals(
                0,
                result.getAmount()
                        .compareTo(new BigDecimal("50.00"))
        );

        assertEquals(
                0,
                result.getBetAmount()
                        .compareTo(new BigDecimal("10.00"))
        );

        assertEquals(symbols, result.getSlotStates());

        InOrder order = inOrder(
                requestValidation,
                bankingRestClient,
                slotEngine,
                entityValidator,
                repository
        );

        order.verify(requestValidation)
                .validatePlayRequest(request);

        order.verify(bankingRestClient)
                .findUserById(1L);

        order.verify(slotEngine)
                .play(new BigDecimal("10.00"));

        order.verify(entityValidator)
                .validate(any(SlotsGameEntity.class));

        order.verify(bankingRestClient)
                .createSlotsTransaction(
                        1L,
                        new BigDecimal("50.00")
                );

        order.verify(repository)
                .save(any(SlotsGameEntity.class));
    }

    @Test
    void shouldStopWhenRequestIsInvalid() {
        SlotsPlayRequest request = createRequest();

        doThrow(new BadSlotsRequestException("Invalid request"))
                .when(requestValidation)
                .validatePlayRequest(request);

        assertThrows(
                BadSlotsRequestException.class,
                () -> playService.play(request)
        );

        verifyNoInteractions(
                bankingRestClient,
                slotEngine,
                entityValidator,
                repository
        );
    }

    @Test
    void shouldNotBookTransactionWhenEntityIsInvalid() {
        SlotsPlayRequest request = createRequest();

        GameResult gameResult = new GameResult(
                true,
                new BigDecimal("50.00"),
                List.of(
                        Symbol.SEVEN,
                        Symbol.SEVEN,
                        Symbol.SEVEN
                )
        );

        when(slotEngine.play(request.getBetAmount()))
                .thenReturn(gameResult);

        doThrow(new BadSlotsRequestException("Invalid entity"))
                .when(entityValidator)
                .validate(any(SlotsGameEntity.class));

        assertThrows(
                BadSlotsRequestException.class,
                () -> playService.play(request)
        );

        verify(bankingRestClient, never())
                .createSlotsTransaction(
                        anyLong(),
                        any(BigDecimal.class)
                );

        verify(repository, never())
                .save(any(SlotsGameEntity.class));
    }

    @Test
    void shouldStopWhenBankingUserDoesNotExist() {
        SlotsPlayRequest request = createRequest();

        doThrow(new BankingUserNotFoundException(request.getUserId()))
                .when(bankingRestClient)
                .findUserById(request.getUserId());

        assertThrows(
                BankingUserNotFoundException.class,
                () -> playService.play(request)
        );

        verify(requestValidation)
                .validatePlayRequest(request);

        verify(bankingRestClient)
                .findUserById(request.getUserId());

        verify(bankingRestClient, never())
                .createSlotsTransaction(
                        anyLong(),
                        any(BigDecimal.class)
                );

        verifyNoInteractions(
                slotEngine,
                entityValidator,
                repository
        );
    }

    @Test
    void shouldNotSaveGameWhenBankingTransactionFails() {
        SlotsPlayRequest request = createRequest();

        GameResult gameResult = new GameResult(
                true,
                new BigDecimal("50.00"),
                List.of(
                        Symbol.SEVEN,
                        Symbol.SEVEN,
                        Symbol.SEVEN
                )
        );

        when(slotEngine.play(request.getBetAmount()))
                .thenReturn(gameResult);

        doThrow(new BadSlotsRequestException(
                "Banking rejected slots transaction"
        ))
                .when(bankingRestClient)
                .createSlotsTransaction(
                        request.getUserId(),
                        gameResult.getAmount()
                );

        assertThrows(
                BadSlotsRequestException.class,
                () -> playService.play(request)
        );

        verify(requestValidation)
                .validatePlayRequest(request);

        verify(bankingRestClient)
                .findUserById(request.getUserId());

        verify(slotEngine)
                .play(request.getBetAmount());

        verify(entityValidator)
                .validate(any(SlotsGameEntity.class));

        verify(bankingRestClient)
                .createSlotsTransaction(
                        request.getUserId(),
                        gameResult.getAmount()
                );

        verify(repository, never())
                .save(any(SlotsGameEntity.class));
    }

    private SlotsPlayRequest createRequest() {
        SlotsPlayRequest request = new SlotsPlayRequest();
        request.setUserId(1L);
        request.setBetAmount(new BigDecimal("10.00"));
        return request;
    }
}