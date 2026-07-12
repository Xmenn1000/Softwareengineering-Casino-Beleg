package casino.slots.controller;

import casino.slots.request.SlotsPlayRequest;
import casino.slots.service.PlayService;
import casino.slots.view.SlotsGameResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayController implements PlayControllerAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayController.class);

    private final PlayService playService;

    public PlayController(PlayService playService) {
        this.playService = playService;
    }

    @Override
    public ResponseEntity<SlotsGameResultDTO> requestPlay(SlotsPlayRequest playRequest) {
        LOGGER.info("play requested by user {}", playRequest.getUserId());
        return ResponseEntity.ok(playService.play(playRequest));
    }
}
