package sosunovych.user.registration.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sosunovych.user.registration.dto.RegisterUserRequestDto;
import sosunovych.user.registration.dto.UpdateFullUserInfoRequestDto;
import sosunovych.user.registration.dto.UpdateUserContactInfoRequestDto;
import sosunovych.user.registration.dto.UserDto;
import sosunovych.user.registration.exception.EmailExistsException;
import sosunovych.user.registration.exception.EntityNotFoundException;
import sosunovych.user.registration.exception.YearInputException;
import sosunovych.user.registration.mapper.UserMapper;
import sosunovych.user.registration.model.User;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    public void clearUserRepositoryList() {
        UserServiceImpl.userRepositoryList.clear();
    }

    @Test
    @DisplayName("Verify registerNewUser() method works")
    public void registerNewUser_ValidRequest_ReturnsUserDto() {
        //given
        RegisterUserRequestDto registerUserRequestDto = createRegistrationRequestDto();
        User expectedUser = createUserFromRegistrationRequest(registerUserRequestDto);
        UserDto expectedUserDto = createUserDtoFromUser(expectedUser);

        when(userMapper.registrationRequestToEntity(registerUserRequestDto))
                .thenReturn(expectedUser);
        when(userMapper.entityToUserDto(any(User.class)))
                .thenReturn(expectedUserDto);

        //when
        UserDto actualUserDto = userServiceImpl.registerNewUser(registerUserRequestDto);

        //then
        assertEquals(expectedUserDto, actualUserDto);

        verify(userMapper, times(1))
                .registrationRequestToEntity(registerUserRequestDto);
        verify(userMapper, times(1))
                .entityToUserDto(any(User.class));
        verifyNoMoreInteractions(userMapper);
    }

    @Test
    @DisplayName("Verify registerNewUser() method throws EmailExistsException for existed email")
    public void registerNewUser_ExistedEmail_ThrowsException() {
        //given
        RegisterUserRequestDto registerUserRequestDto = createRegistrationRequestDto();

        UserServiceImpl.userRepositoryList.add(
                new User().setUserId(1).setEmail(registerUserRequestDto.getEmail()));

        //when
        Exception exception = assertThrows(EmailExistsException.class,
                () -> userServiceImpl.registerNewUser(registerUserRequestDto)
        );

        //then
        String expectedMessage = "Email " + registerUserRequestDto.getEmail()
                + " is already in use. Please, try with another one.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verifyNoInteractions(userMapper);
    }

    @Test
    @DisplayName("Verify searchUsers() method works")
    public void searchUsers_ValidYears_ReturnsUserDtoList() {
        //given
        RegisterUserRequestDto registerUserRequestDto = createRegistrationRequestDto();

        User user1 = createUserFromRegistrationRequest(registerUserRequestDto);
        user1.setUserId(1)
                .setEmail("email1@example.com")
                .setBirthDate(LocalDate.parse("2001-01-01"));
        UserServiceImpl.userRepositoryList.add(user1);

        User user2 = createUserFromRegistrationRequest(registerUserRequestDto);
        user2.setUserId(2)
                .setEmail("email2@example.com")
                .setBirthDate(LocalDate.parse("2002-02-02"));
        UserServiceImpl.userRepositoryList.add(user2);

        User user3 = createUserFromRegistrationRequest(registerUserRequestDto);
        user3.setUserId(3)
                .setEmail("email3@example.com")
                .setBirthDate(LocalDate.parse("2003-03-03"));
        UserServiceImpl.userRepositoryList.add(user3);

        Integer fromYear = 2003;
        Integer toYear = 2005;

        UserDto userDto3 = createUserDtoFromUser(user3);

        List<UserDto> expectedUserDtoList = List.of(userDto3);

        when(userMapper.entityToUserDto(any(User.class)))
                .thenReturn(userDto3);

        //when
        List<UserDto> actualUserDtoList = userServiceImpl.searchUsers(fromYear, toYear);

        //then
        assertEquals(expectedUserDtoList.size(), actualUserDtoList.size());
        assertIterableEquals(expectedUserDtoList, actualUserDtoList);

        verify(userMapper, times(1)).entityToUserDto(
                any(User.class));
        verifyNoMoreInteractions(userMapper);
    }

    @Test
    @DisplayName("Verify searchUsers() method throws YearInputException for invalid years")
    public void searchUsers_InvalidYears_ThrowsException() {
        //given
        Integer fromYear1 = -100;
        Integer toYear1 = 100;

        Integer fromYear2 = 2000;
        Integer toYear2 = 1990;

        //when
        Exception exception1 = assertThrows(YearInputException.class,
                () -> userServiceImpl.searchUsers(fromYear1, toYear1)
        );

        Exception exception2 = assertThrows(YearInputException.class,
                () -> userServiceImpl.searchUsers(fromYear2, toYear2)
        );

        //then
        String expectedMessage1 = "Values 'fromYear' and/or 'toYear' can not be negative.";
        String expectedMessage2 = "'fromYear' value must be equal or less than 'toYear'";
        String actualMessage1 = exception1.getMessage();
        String actualMessage2 = exception2.getMessage();

        assertEquals(expectedMessage1, actualMessage1);
        assertEquals(expectedMessage2, actualMessage2);

        verifyNoInteractions(userMapper);
    }

    @Test
    @DisplayName("Verify updateFullUserInfo() method works")
    public void updateFullUserInfo_ValidUserIdAndRequest_ReturnsUserDto() {
        //given
        RegisterUserRequestDto registerUserRequestDto = createRegistrationRequestDto();
        User user = createUserFromRegistrationRequest(registerUserRequestDto);
        UserServiceImpl.userRepositoryList.add(user);

        int userId = user.getUserId();

        UpdateFullUserInfoRequestDto updateRequestDto = new UpdateFullUserInfoRequestDto()
                .setEmail("newEmail@example.com")
                .setFirstName("newName")
                .setLastName("newLastName")
                .setBirthDate("1999-09-09")
                .setAddress("New address")
                .setPhoneNumber("New phone 123");

        User updatedUser = new User()
                .setUserId(user.getUserId())
                .setEmail(updateRequestDto.getEmail())
                .setFirstName(updateRequestDto.getFirstName())
                .setLastName(updateRequestDto.getLastName())
                .setBirthDate(LocalDate.parse(updateRequestDto.getBirthDate()))
                .setAddress(updateRequestDto.getAddress())
                .setPhoneNumber(updateRequestDto.getPhoneNumber())
                .setDeleted(user.isDeleted());

        UserDto expectedUserDto = createUserDtoFromUser(updatedUser);

        when(userMapper.updateFullUserInfo(
                UserServiceImpl.userRepositoryList.get(0), updateRequestDto))
                .thenReturn(updatedUser);
        when(userMapper.entityToUserDto(any(User.class)))
                .thenReturn(expectedUserDto);

        //when
        UserDto actualUserDto = userServiceImpl.updateFullUserInfo(userId, updateRequestDto);

        //then
        assertEquals(expectedUserDto, actualUserDto);

        verify(userMapper, times(1)).updateFullUserInfo(
                UserServiceImpl.userRepositoryList.get(0), updateRequestDto);
        verify(userMapper, times(1)).entityToUserDto(
                any(User.class));
        verifyNoMoreInteractions(userMapper);
    }

    @Test
    @DisplayName("Verify updateFullUserInfo() method throws "
            + "EntityNotFoundException for invalid userId")
    public void updateFullUserInfo_InvalidUserId_ThrowsException() {
        //given
        RegisterUserRequestDto registerUserRequestDto = createRegistrationRequestDto();
        User user = createUserFromRegistrationRequest(registerUserRequestDto);
        UserServiceImpl.userRepositoryList.add(user);

        int userId = user.getUserId() + 1000;

        UpdateFullUserInfoRequestDto updateRequestDto = new UpdateFullUserInfoRequestDto()
                .setEmail("newEmail@example.com")
                .setFirstName("newName")
                .setLastName("newLastName")
                .setBirthDate("1999-09-09")
                .setAddress("New address")
                .setPhoneNumber("New phone 123");

        //when
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> userServiceImpl.updateFullUserInfo(userId, updateRequestDto)
        );

        //then
        String expectedMessage = "User with id " + userId + " was not found.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verifyNoInteractions(userMapper);
    }

    @Test
    @DisplayName("Verify updateFullUserInfo() method throws EmailExistsException for existed email")
    public void updateFullUserInfo_ExistedEmail_ThrowsException() {
        //given
        RegisterUserRequestDto registerUserRequestDto = createRegistrationRequestDto();

        User user1 = createUserFromRegistrationRequest(registerUserRequestDto);
        user1.setUserId(1).setEmail("email1@example.com");
        UserServiceImpl.userRepositoryList.add(user1);

        User user2 = createUserFromRegistrationRequest(registerUserRequestDto);
        user2.setUserId(2).setEmail("email2@example.com");
        UserServiceImpl.userRepositoryList.add(user2);

        int userId = user2.getUserId();

        UpdateFullUserInfoRequestDto updateRequestDto = new UpdateFullUserInfoRequestDto()
                .setEmail(user1.getEmail())
                .setFirstName("newName")
                .setLastName("newLastName")
                .setBirthDate("1999-09-09")
                .setAddress("New address")
                .setPhoneNumber("New phone 123");

        //when
        Exception exception = assertThrows(EmailExistsException.class,
                () -> userServiceImpl.updateFullUserInfo(userId, updateRequestDto)
        );

        //then
        String expectedMessage = "Email " + updateRequestDto.getEmail()
                + " is already in use. Please, try with another one.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verifyNoInteractions(userMapper);
    }

    @Test
    @DisplayName("Verify updateUserContactInfo() method works")
    public void updateUserContactInfo_ValidUserIdAndRequest_ReturnsUserDto() {
        //given
        RegisterUserRequestDto registerUserRequestDto = createRegistrationRequestDto();
        User user = createUserFromRegistrationRequest(registerUserRequestDto);
        UserServiceImpl.userRepositoryList.add(user);

        int userId = user.getUserId();

        UpdateUserContactInfoRequestDto updateRequestDto =
                new UpdateUserContactInfoRequestDto()
                        .setAddress("New address")
                        .setPhoneNumber("New phone 123");

        User updatedUser = new User()
                .setUserId(user.getUserId())
                .setEmail(user.getEmail())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setBirthDate(user.getBirthDate())
                .setAddress(updateRequestDto.getAddress())
                .setPhoneNumber(updateRequestDto.getPhoneNumber())
                .setDeleted(user.isDeleted());

        UserDto expectedUserDto = createUserDtoFromUser(updatedUser);

        when(userMapper.updateUserContactInfo(
                UserServiceImpl.userRepositoryList.get(0), updateRequestDto))
                .thenReturn(updatedUser);
        when(userMapper.entityToUserDto(any(User.class)))
                .thenReturn(expectedUserDto);

        //when
        UserDto actualUserDto = userServiceImpl.updateUserContactInfo(userId, updateRequestDto);

        //then
        assertEquals(expectedUserDto, actualUserDto);

        verify(userMapper, times(1)).updateUserContactInfo(
                UserServiceImpl.userRepositoryList.get(0), updateRequestDto);
        verify(userMapper, times(1)).entityToUserDto(
                any(User.class));
        verifyNoMoreInteractions(userMapper);
    }

    @Test
    @DisplayName("Verify updateUserContactInfo() method throws "
            + "EntityNotFoundException for invalid userId")
    public void updateUserContactInfo_InvalidUserId_ThrowsException() {
        //given
        RegisterUserRequestDto registerUserRequestDto = createRegistrationRequestDto();
        User user = createUserFromRegistrationRequest(registerUserRequestDto);
        UserServiceImpl.userRepositoryList.add(user);

        int userId = user.getUserId() + 1000;

        UpdateUserContactInfoRequestDto updateRequestDto =
                new UpdateUserContactInfoRequestDto()
                        .setAddress("New address")
                        .setPhoneNumber("New phone 123");

        //when
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> userServiceImpl.updateUserContactInfo(userId, updateRequestDto)
        );

        //then
        String expectedMessage = "User with id " + userId + " was not found.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verifyNoInteractions(userMapper);
    }

    @Test
    @DisplayName("Verify deleteUserById() method works")
    public void deleteUserById_ValidUserId_UserIsDeletedEqualsTrue() {
        //given
        UserServiceImpl.userRepositoryList.add(new User().setUserId(1).setDeleted(false));

        int userId = 1;

        //when
        userServiceImpl.deleteUserById(userId);

        //then
        assertEquals(1, UserServiceImpl.userRepositoryList.size());
        assertTrue(UserServiceImpl.userRepositoryList.get(0).isDeleted());

        verifyNoInteractions(userMapper);
    }

    @Test
    @DisplayName("Verify deleteUserById() method throws EntityNotFoundException for invalid userId")
    public void deleteUserById_InvalidUserId_ThrowsException() {
        //given
        int userId = 1;

        //when
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> userServiceImpl.deleteUserById(userId)
        );

        //then
        String expectedMessage = "User with id " + userId + " was not found.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verifyNoInteractions(userMapper);
    }

    private RegisterUserRequestDto createRegistrationRequestDto() {
        return new RegisterUserRequestDto()
                .setEmail("test@example.com")
                .setFirstName("Name")
                .setLastName("LastName")
                .setBirthDate("2000-01-01")
                .setAddress("Address")
                .setPhoneNumber("1234567890");
    }

    private User createUserFromRegistrationRequest(RegisterUserRequestDto requestDto) {
        return new User()
                .setUserId(1)
                .setEmail(requestDto.getEmail())
                .setFirstName(requestDto.getFirstName())
                .setLastName(requestDto.getLastName())
                .setBirthDate(LocalDate.parse(requestDto.getBirthDate()))
                .setAddress(requestDto.getAddress())
                .setPhoneNumber(requestDto.getPhoneNumber())
                .setDeleted(false);
    }

    private UserDto createUserDtoFromUser(User user) {
        return new UserDto()
                .setUserId(1)
                .setEmail(user.getEmail())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setBirthDate(user.getBirthDate())
                .setAddress(user.getAddress())
                .setPhoneNumber(user.getPhoneNumber());
    }
}
