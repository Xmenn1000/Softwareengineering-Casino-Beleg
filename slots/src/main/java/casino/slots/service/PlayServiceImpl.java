package casino.slots.service;

import casino.slots.domain.dto.GameResult;
import casino.slots.domain.machine.SlotEngine;
import casino.slots.model.SlotsGameEntity;
import casino.slots.model.SlotsGameEntityFactory;
import casino.slots.repository.SlotsGameRepository;
import casino.slots.request.SlotsPlayRequest;
import casino.slots.restClient.BankingRestClient;
import casino.slots.validation.SlotsGameEntityValidator;
import casino.slots.validation.SlotsRequestValidation;
import casino.slots.view.SlotsGameDTO;
import casino.slots.view.SlotsGameResultDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PlayServiceImpl implements PlayService {

    private final SlotsGameRepository slotsGameRepository;
    private final SlotsRequestValidation slotsRequestValidation;
    private final SlotsGameEntityValidator slotsGameEntityValidator;
    private final BankingRestClient bankingRestClient;
    private final SlotEngine slotEngine;

    public PlayServiceImpl(SlotsGameRepository slotsGameRepository, SlotEngine slotMachine, SlotsRequestValidation slotsRequestValidation, SlotsGameEntityValidator slotsGameEntityValidator, BankingRestClient bankingRestClient, SlotEngine slotEngine) {
        this.slotsGameRepository = slotsGameRepository;
        this.slotsRequestValidation = slotsRequestValidation;
        this.slotsGameEntityValidator = slotsGameEntityValidator;
        this.bankingRestClient = bankingRestClient;
        this.slotEngine = slotEngine;
    }

    @Override
    public SlotsGameResultDTO play(SlotsPlayRequest playRequest) {

        //TODO: VALIDATE USER AND ADD MORE TO PLAY SERVICE (LOOK AT ROULLETT SERVICE INTERFACE)

        slotsRequestValidation.validatePlayRequest(playRequest);

        bankingRestClient.findUserById(playRequest.getUserId());

        GameResult gameResult = slotEngine.play(playRequest.getBetAmount());

        SlotsGameEntity game = SlotsGameEntityFactory.create(playRequest.getUserId(), gameResult.isWinning(), gameResult.getAmount(), playRequest.getBetAmount(), gameResult.getSlotStates());

        slotsGameEntityValidator.validate(game);

        bankingRestClient.createSlotsTransaction(game.getUserId(), game.getAmount());

        SlotsGameEntity savedGame = slotsGameRepository.save(game);

        return new SlotsGameResultDTO(savedGame.getUserId(), savedGame.isWinning(), savedGame.getAmount(), savedGame.getSlotStates(), savedGame.getBetAmount());

    }


}
