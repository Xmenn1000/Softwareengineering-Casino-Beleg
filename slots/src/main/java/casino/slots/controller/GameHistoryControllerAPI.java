package casino.slots.controller;

import casino.slots.view.SlotsGameDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "Games", description = "History of played games")
@RequestMapping("/casino/slots/api")
public interface GameHistoryControllerAPI {

    @Operation(
            summary = "Get all played games",
            description = "Returns all played games. Empty array if there are none."
    )
    @ApiResponse(responseCode = "200", description = "List of games (may be empty)")
    @GetMapping("/stats/games")
    ResponseEntity<List<SlotsGameDTO>> findAll();

    @Operation(
            summary = "Get a played game by ID",
            description = "Returns the stat entry of a single played game."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Game found"),
            @ApiResponse(responseCode = "404", description = "Game not found", content = @Content)
    })
    @GetMapping("/stat/{gameId}")
    ResponseEntity<SlotsGameDTO> findById(
            @Parameter(description = "Game ID", example = "42")
            @PathVariable Long gameId
    );

    @Operation(
            summary = "Delete a played game",
            description = "Deletes the stat entry of a played game. Aggregated statistics change retroactively."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Game deleted"),
            @ApiResponse(responseCode = "404", description = "Game not found", content = @Content)
    })
    @DeleteMapping("/stat/{gameId}")
    ResponseEntity<SlotsGameDTO> deleteById(
            @Parameter(description = "Game ID", example = "42")
            @PathVariable Long gameId
    );
}
