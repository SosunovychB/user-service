package sosunovych.user.registration.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sosunovych.user.registration.dto.RegisterUserRequestDto;
import sosunovych.user.registration.dto.UpdateFullUserInfoRequestDto;
import sosunovych.user.registration.dto.UpdateUserContactInfoRequestDto;
import sosunovych.user.registration.dto.UserDto;
import sosunovych.user.registration.exception.EmailExistsException;
import sosunovych.user.registration.exception.EntityNotFoundException;
import sosunovych.user.registration.exception.YearInputException;
import sosunovych.user.registration.mapper.UserMapper;
import sosunovych.user.registration.model.User;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    //Mock of a data persistence layer
    static final List<User> userRepositoryList = new ArrayList<>();
    private final UserMapper userMapper;

    @Override
    public UserDto registerNewUser(RegisterUserRequestDto registerUserRequestDto) {
        checkIfEmailIsUnique(registerUserRequestDto.getEmail());
        User savedUser = saveNewUser(registerUserRequestDto);
        return userMapper.entityToUserDto(savedUser);
    }

    @Override
    public List<UserDto> searchUsers(Integer fromYear, Integer toYear) {
        checkIfInputYearsAreValid(fromYear, toYear);
        return userRepositoryList.stream()
                .filter(user -> {
                    int userBirthYear = user.getBirthDate().getYear();
                    return !user.isDeleted()
                            && (fromYear == null || userBirthYear >= fromYear)
                            && (toYear == null || userBirthYear <= toYear);
                })
                .map(userMapper::entityToUserDto)
                .toList();
    }

    @Override
    public UserDto updateFullUserInfo(int userId, UpdateFullUserInfoRequestDto requestDto) {
        User userById = getUserById(userId);
        checkIfEmailIsUnique(requestDto.getEmail());
        User updatedUser = userMapper.updateFullUserInfo(userById, requestDto);
        return userMapper.entityToUserDto(updatedUser);
    }

    @Override
    public UserDto updateUserContactInfo(int userId, UpdateUserContactInfoRequestDto requestDto) {
        User userById = getUserById(userId);
        User updatedUser = userMapper.updateUserContactInfo(userById, requestDto);
        return userMapper.entityToUserDto(updatedUser);
    }

    @Override
    public void deleteUserById(int userId) {
        getUserById(userId).setDeleted(true);
    }

    private void checkIfEmailIsUnique(String email) {
        boolean isNotUnique = userRepositoryList.stream()
                .anyMatch(user -> user.getEmail().equals(email));

        if (isNotUnique) {
            throw new EmailExistsException("Email " + email + " is already in use. "
                    + "Please, try with another one.");
        }
    }

    private User saveNewUser(RegisterUserRequestDto registerUserRequestDto) {
        User newUser = userMapper.registrationRequestToEntity(registerUserRequestDto);
        newUser.setUserId(userRepositoryList.size() + 1);

        userRepositoryList.add(newUser);
        return userRepositoryList.get(newUser.getUserId() - 1);
    }

    private void checkIfInputYearsAreValid(Integer fromYear, Integer toYear) {
        if (fromYear != null && toYear != null && (fromYear < 0 || toYear < 0)) {
            throw new YearInputException("Values 'fromYear' and/or 'toYear' "
                    + "can not be negative.");
        }

        if (fromYear != null && toYear != null && (toYear < fromYear)) {
            throw new YearInputException("'fromYear' value must be equal or less than 'toYear'");
        }
    }

    private User getUserById(int userId) {
        Optional<User> userOptionalById = userRepositoryList.stream()
                .filter(user -> user.getUserId() == userId)
                .findAny();
        return userOptionalById.orElseThrow(
                () -> new EntityNotFoundException("User with id " + userId + " was not found.")
        );
    }
}
