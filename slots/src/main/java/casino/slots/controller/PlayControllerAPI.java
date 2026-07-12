package casino.slots.controller;

import casino.slots.request.SlotsPlayRequest;
import casino.slots.view.SlotsGameResultDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Play", description = "Play a round of slots")
@RequestMapping("/casino/slots/api")
public interface PlayControllerAPI {

    @Operation(
            summary = "Play one round",
            description = "Places the stake, spins the three slots and returns the outcome of the round."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Round played"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request body or insufficient balance", content = @Content)
    })
    @PostMapping("/play")
    ResponseEntity<SlotsGameResultDTO> requestPlay(@Valid @RequestBody SlotsPlayRequest playRequest);
}
