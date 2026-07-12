package casino.slots.controller;

import casino.slots.request.SlotsPlayRequest;
import casino.slots.service.interfaces.SlotsGameService;
import casino.slots.view.SlotsGameResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayController implements PlayControllerAPI {

    private final SlotsGameService playService;

    public PlayController(SlotsGameService playService) {
        this.playService = playService;
    }

    @Override
    public SlotsGameResultDTO requestPlay(SlotsPlayRequest playRequest) {

        Logger logger = LoggerFactory.getLogger(PlayController.class);
        logger.info(playRequest.toString());
        return playService.play(playRequest);
    }
}
