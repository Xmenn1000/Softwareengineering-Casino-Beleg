package casino.slots.controller;

import casino.slots.request.SlotsPlayRequest;
import casino.slots.view.SlotsGameResultDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/casino/slots/api")
public interface PlayControllerAPI {

    @PostMapping("/play")
    SlotsGameResultDTO requestPlay(@RequestBody SlotsPlayRequest playRequest);
}
