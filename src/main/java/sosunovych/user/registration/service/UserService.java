package sosunovych.user.registration.service;

import java.util.List;
import sosunovych.user.registration.dto.RegisterUserRequestDto;
import sosunovych.user.registration.dto.UpdateUserContactInfoRequestDto;
import sosunovych.user.registration.dto.UserDto;

public interface UserService {
    UserDto registerNewUser(RegisterUserRequestDto registerUserRequestDto);

    List<UserDto> searchUsers(Integer fromYear, Integer toYear);

    UserDto updateUserContactInfo(int userId, UpdateUserContactInfoRequestDto requestDto);

    void deleteUserById(int userId);
}
