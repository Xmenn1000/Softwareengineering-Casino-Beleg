package casino.slots.exeptions;

import org.springframework.http.HttpStatus;

public class SlotsGameNotFoundException extends HttpException {

    public SlotsGameNotFoundException(Long gameId) {
        super(
                HttpStatus.NOT_FOUND,
                "Slots game with id " + gameId + " not found"
        );
    }
}