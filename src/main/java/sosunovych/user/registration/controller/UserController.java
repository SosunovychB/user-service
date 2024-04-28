package sosunovych.user.registration.controller;

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

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto registerNewUser(
            @Valid @RequestBody RegisterUserRequestDto registerUserRequestDto) {
        return userService.registerNewUser(registerUserRequestDto);
    }
}
