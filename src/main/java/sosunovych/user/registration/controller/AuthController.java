package sosunovych.user.registration.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sosunovych.user.registration.dto.RegisterUserRequestDto;
import sosunovych.user.registration.dto.UserDto;
import sosunovych.user.registration.service.UserService;

@Tag(name = "Auth management", description = "Endpoint for registration")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Endpoint for registration",
            description = "Endpoint for registration")
    public UserDto registerNewUser(
            @Valid @RequestBody RegisterUserRequestDto registerUserRequestDto) {
        return userService.registerNewUser(registerUserRequestDto);
    }
}
