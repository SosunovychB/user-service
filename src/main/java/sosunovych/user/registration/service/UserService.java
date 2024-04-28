package sosunovych.user.registration.service;

import sosunovych.user.registration.dto.RegisterUserRequestDto;
import sosunovych.user.registration.dto.UserDto;

public interface UserService {
    UserDto registerNewUser(RegisterUserRequestDto registerUserRequestDto);
}
