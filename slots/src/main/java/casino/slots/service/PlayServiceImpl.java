package casino.slots.service;

import casino.slots.domain.dto.GameResult;
import casino.slots.domain.machine.SlotEngine;
import casino.slots.request.SlotsPlayRequest;
import casino.slots.view.SlotsGameResultDTO;
import org.springframework.stereotype.Service;

@Service
public class PlayServiceImpl implements PlayService {

    private final SlotEngine slotMachine;

    public PlayServiceImpl(SlotEngine slotMachine) {
        this.slotMachine = slotMachine;
    }

    @Override
    public SlotsGameResultDTO play(SlotsPlayRequest playRequest) {

        //TODO: VALIDATE USER

        GameResult result = slotMachine.play(playRequest.getAmount());
        return new SlotsGameResultDTO(playRequest.getUserId(), result.isWinning(), result.getAmount(), result.getSlotStates());
    }
}
