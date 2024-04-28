package sosunovych.user.registration.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sosunovych.user.registration.dto.UpdateFullUserInfoRequestDto;
import sosunovych.user.registration.dto.UpdateUserContactInfoRequestDto;
import sosunovych.user.registration.dto.UserDto;
import sosunovych.user.registration.service.UserService;

@Tag(name = "User management", description = "Endpoints for managing users")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Search users by birth years",
            description = "Search users by birth years")
    public List<UserDto> searchUsers(@RequestParam(required = false) Integer fromYear,
                                     @RequestParam(required = false) Integer toYear) {
        return userService.searchUsers(fromYear, toYear);
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update full user info by id",
            description = "Update full user info by id")
    public UserDto updateFullUserInfo(@PathVariable int userId,
            @Valid @RequestBody UpdateFullUserInfoRequestDto updateFullUserInfoRequestDto) {
        return userService.updateFullUserInfo(userId, updateFullUserInfoRequestDto);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update user contact info by id",
            description = "Update user contact info by id")
    public UserDto updateUserContactInfo(@PathVariable int userId,
            @RequestBody UpdateUserContactInfoRequestDto requestDto) {
        return userService.updateUserContactInfo(userId, requestDto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete user by id",
            description = "Delete user by id")
    public void deleteUserById(@PathVariable int userId) {
        userService.deleteUserById(userId);
    }
}
