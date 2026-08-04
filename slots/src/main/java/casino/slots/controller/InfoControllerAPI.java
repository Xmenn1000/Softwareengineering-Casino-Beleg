package casino.slots.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Infos", description = "Infos over the Slot Service")
@RequestMapping("/casino/slots/api/info")
public interface InfoControllerAPI {

    @Operation(
            summary = "Get rules of the game",
            description = "returns a String of the rules"
    )
    @ApiResponse(responseCode = "200", description = "String with rules")
    @GetMapping("/rules")
    ResponseEntity<String> getRules();

    @Operation(
            summary = "Get a played game by ID",
            description = "Returns the stat entry of a single played game."
    )
    @ApiResponse(responseCode = "200", description = "String with Chances")
    @GetMapping("/chances")
    ResponseEntity<String> getChances();
}
