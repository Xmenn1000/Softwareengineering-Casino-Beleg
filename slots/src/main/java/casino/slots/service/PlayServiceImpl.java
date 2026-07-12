package casino.slots.service;

import casino.slots.machine.GameResult;
import casino.slots.machine.SlotMachine;
import casino.slots.request.SlotsPlayRequest;
import casino.slots.view.SlotsGameResultDTO;
import org.springframework.stereotype.Service;

@Service
public class PlayServiceImpl implements PlayService {

    private final SlotMachine slotMachine;

    public PlayServiceImpl(SlotMachine slotMachine) {
        this.slotMachine = slotMachine;
    }

    @Override
    public SlotsGameResultDTO play(SlotsPlayRequest playRequest) {

        //TODO: VALIDATE USER

        GameResult result = slotMachine.play(playRequest.getAmount());
        return new SlotsGameResultDTO(playRequest.getUserId(), result.isWinning(), result.getAmount(), result.getSlotStates());
    }
}
