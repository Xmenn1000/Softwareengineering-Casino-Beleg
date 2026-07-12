package casino.slots.controller;

import casino.slots.view.SlotsStatsDTO;
import casino.slots.view.SlotsStatsUserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Stats", description = "Win and loss statistics")
@RequestMapping("/casino/slots/api")
public interface StatsControllerAPI {

    @Operation(
            summary = "Get overall statistics",
            description = "Returns aggregated statistics over all played games."
    )
    @ApiResponse(responseCode = "200", description = "Aggregated statistics")
    @GetMapping("/stats")
    ResponseEntity<SlotsStatsDTO> getStats();

    @Operation(
            summary = "Get statistics for a user",
            description = "Returns aggregated statistics over all games played by the given user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User statistics"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping("/stats/user/{userId}")
    ResponseEntity<SlotsStatsUserDTO> getStatsByUserId(
            @Parameter(description = "User ID", example = "42")
            @PathVariable Long userId
    );
}
