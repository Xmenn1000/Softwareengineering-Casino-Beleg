package casino.slots.controller;

import casino.slots.service.InfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController implements InfoControllerAPI {

    private final InfoService infoService;

    public InfoController(InfoService infoService) {
        this.infoService = infoService;
    }

    @Override
    public ResponseEntity<String> getRules() {
        return ResponseEntity.ok().body(infoService.getRules());
    }

    @Override
    public ResponseEntity<String> getChances() {
        return ResponseEntity.ok().body(infoService.getChances());
    }
}
