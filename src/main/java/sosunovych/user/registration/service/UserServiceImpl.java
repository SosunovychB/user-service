package sosunovych.user.registration.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sosunovych.user.registration.dto.RegisterUserRequestDto;
import sosunovych.user.registration.dto.UserDto;
import sosunovych.user.registration.exception.EmailExistsException;
import sosunovych.user.registration.mapper.UserMapper;
import sosunovych.user.registration.model.User;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    //Mock of a data persistence layer
    private final List<User> userRepositoryList = new ArrayList<>();
    private final UserMapper userMapper;

    @Override
    public UserDto registerNewUser(RegisterUserRequestDto registerUserRequestDto) {
        checkIfEmailIsUnique(registerUserRequestDto.getEmail());

        User newUser = userMapper.registrationRequestToEntity(registerUserRequestDto);
        newUser.setUserId(userRepositoryList.size() + 1);

        userRepositoryList.add(newUser);
        User savedUser = userRepositoryList.get(newUser.getUserId() - 1);

        return userMapper.entityToUserDto(savedUser);
    }

    private void checkIfEmailIsUnique(String email) {
        boolean isNotUnique = userRepositoryList.stream()
                .anyMatch(user -> user.getEmail().equals(email));

        if (isNotUnique) {
            throw new EmailExistsException("Email " + email + " is already in use. "
                    + "Please, try with another one.");
        }
    }
}
