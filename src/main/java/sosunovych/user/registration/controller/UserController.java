package sosunovych.user.registration.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sosunovych.user.registration.dto.UserDto;
import sosunovych.user.registration.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> searchUsers(@RequestParam(required = false) Integer fromYear,
                                     @RequestParam(required = false) Integer toYear) {
        return userService.searchUsers(fromYear, toYear);
    }
}
