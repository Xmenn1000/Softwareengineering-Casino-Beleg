package casino.slots.controller;

import casino.slots.service.GameHistoryService;
import casino.slots.view.SlotsGameDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GameHistoryController implements GameHistoryControllerAPI {

    private final GameHistoryService gameHistoryService;

    public GameHistoryController(GameHistoryService gameHistoryService) {
        this.gameHistoryService = gameHistoryService;
    }

    @Override
    public ResponseEntity<List<SlotsGameDTO>> findAll() {
        return ResponseEntity.ok(gameHistoryService.findAll());
    }

    @Override
    public ResponseEntity<SlotsGameDTO> findById(Long gameId) {
        return ResponseEntity.ok(gameHistoryService.findById(gameId));
    }

    @Override
    public ResponseEntity<SlotsGameDTO> deleteById(Long gameId) {
        return ResponseEntity.ok(gameHistoryService.deleteById(gameId));
    }
}
