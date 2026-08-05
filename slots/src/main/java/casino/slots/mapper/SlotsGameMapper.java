package casino.slots.mapper;

import casino.slots.model.SlotsGameEntity;
import casino.slots.view.SlotsGameDTO;

public final class SlotsGameMapper {

    private SlotsGameMapper() {
    }

    public static SlotsGameDTO toDTO(SlotsGameEntity entity) {
        return new SlotsGameDTO(
                entity.getId(),
                entity.getUserId(),
                entity.isWinning(),
                entity.getAmount(),
                entity.getBetAmount(),
                entity.getSlotStates()
        );
    }
}