package casino.banking.controller.user;

import casino.banking.request.user.UserRequestDTO;
import casino.banking.view.user.UserDTO;
import casino.banking.view.user.UserDeleteDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigInteger;
import java.util.List;

@Tag(name = "User", description = "Manage user accounts")
@Validated
@RequestMapping("/casino/bank/api")
public interface UserApi {

    @GetMapping("/user/{id}")
    ResponseEntity<UserDTO> findById(
            @Parameter(description = "User ID", example = "42")
            @PathVariable Long id
    );

    @Operation(
            summary = "Get all users",
            description = "Returns all users. Empty array if there are none."
    )
    @ApiResponse(responseCode = "200", description = "List of users (may be empty)")
    @GetMapping("/users")
    ResponseEntity<List<UserDTO>> findAll();

    @Operation(
            summary = "Create a user",
            description = "Creates a new user with the given first and last name. Starts with a balance of 0."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content)
    })
    @PostMapping("/user")
    ResponseEntity<UserDTO> create(@Valid @RequestBody UserRequestDTO userRequest);

    @Operation(
            summary = "Replace a user by ID",
            description = "Updates the first and last name of an existing user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content)
    })
    @PutMapping("/user/{id}")
    ResponseEntity<UserDTO> replaceById(
            @Parameter(description = "User ID", example = "42")
            @PathVariable Long id,
            @RequestBody UserRequestDTO userRequest
    );

    @Operation(
            summary = "Delete a user",
            description = "Deletes the user. The returned object does not include the ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @DeleteMapping("/user/{id}")
    ResponseEntity<UserDeleteDTO> deleteById(
            @Parameter(description = "User ID", example = "42")
            @PathVariable Long id
    );

    @Operation(
            summary = "Deposit funds",
            description = "Adds the given amount to the user's balance. Amount and decimals must be non-negative, and decimals must not exceed 2."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Balance updated"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid amount or decimals", content = @Content)
    })
    @PostMapping("/user/{userId}/deposit/{amount}/{decimals}")
    ResponseEntity<UserDTO> depositBalanceById(
            @Parameter(description = "User ID", example = "42")
            @PathVariable Long userId,
            @Parameter(description = "Deposit amount", example = "100")
            @Min(0) @PathVariable BigInteger amount,
            @Parameter(description = "Positiv Number of decimals (max 2)", example = "2")
            @Min(0) @Max(99) @PathVariable int decimals
    );

    @PostMapping("/user/{userId}/withDraw/{amount}/{decimals}")
    ResponseEntity<UserDTO> withDrawById(
            @Parameter(description = "User ID", example = "42")
            @PathVariable Long userId,
            @Parameter(description = "Withdraw amount", example = "100")
            @Min(0) @PathVariable BigInteger amount,
            @Parameter(description = "Positiv Number of decimals (max 2)", example = "2")
            @Min(0) @Max(99) @PathVariable int decimals
    );
}
