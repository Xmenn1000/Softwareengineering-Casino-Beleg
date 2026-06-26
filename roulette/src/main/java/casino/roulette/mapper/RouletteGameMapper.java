package casino.roulette.mapper;

import casino.roulette.model.RouletteGameEntity;
import casino.roulette.view.RouletteGameDTO;
import casino.roulette.view.RoulettePlayResultDTO;

public final class RouletteGameMapper {

    private RouletteGameMapper() {
    }

    public static RouletteGameDTO toGameDto(RouletteGameEntity entity) {
        return new RouletteGameDTO(
                entity.getId(),
                entity.getUser(),
                entity.isWinning(),
                entity.getAmount(),
                entity.getBetAmount(),
                entity.getBetType(),
                entity.getBetValue(),
                entity.getBallPosition()
        );
    }

    public static RoulettePlayResultDTO toPlayResultDto(RouletteGameEntity entity) {
        return new RoulettePlayResultDTO(
                entity.getUser(),
                entity.isWinning(),
                entity.getAmount(),
                entity.getBetAmount(),
                entity.getBetType(),
                entity.getBetValue(),
                entity.getBallPosition()
        );
    }
}
