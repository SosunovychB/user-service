package sosunovych.user.registration.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import sosunovych.user.registration.dto.UpdateFullUserInfoRequestDto;
import sosunovych.user.registration.dto.UpdateUserContactInfoRequestDto;
import sosunovych.user.registration.dto.UserDto;
import sosunovych.user.registration.service.UserService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

    @Test
    @DisplayName("Search users by birth years")
    void searchUsers_ValidYears_ReturnsJsonUserDtoList() throws Exception {
        //given
        UserDto user1 = new UserDto()
                .setUserId(1)
                .setEmail("bob@example.com")
                .setFirstName("Bob")
                .setLastName("Alison")
                .setBirthDate(LocalDate.parse("2000-01-01"))
                .setAddress("New York")
                .setPhoneNumber("1234567890");

        List<UserDto> userList = List.of(user1);

        given(userService.searchUsers(1995, 2005)).willReturn(userList);

        //when
        mockMvc.perform(get("/api/users")
                        .param("fromYear", "1995")
                        .param("toYear", "2005"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(user1.getUserId()))
                .andExpect(jsonPath("$[0].firstName").value(user1.getFirstName()));
    }

    @Test
    @DisplayName("Update full user info by id")
    public void updateFullUserInfo_ValidIdAndRequest_ReturnJsonUpdatedUser() throws Exception {
        // given
        int userId = 1;
        UpdateFullUserInfoRequestDto requestDto = new UpdateFullUserInfoRequestDto()
                .setEmail("updated_email@example.com")
                .setFirstName("UpdatedFirstName")
                .setLastName("UpdatedLastName")
                .setBirthDate("2005-01-01")
                .setAddress("UpdatedAddress")
                .setPhoneNumber("0987654321");

        UserDto updatedUserDto = new UserDto()
                .setUserId(userId)
                .setEmail(requestDto.getEmail())
                .setFirstName(requestDto.getFirstName())
                .setLastName(requestDto.getLastName())
                .setBirthDate(LocalDate.parse(requestDto.getBirthDate()))
                .setAddress(requestDto.getAddress())
                .setPhoneNumber(requestDto.getPhoneNumber());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        given(userService.updateFullUserInfo(eq(userId), any(UpdateFullUserInfoRequestDto.class)))
                .willReturn(updatedUserDto);

        // when + then
        mockMvc.perform(put("/api/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.email").value(requestDto.getEmail()))
                .andExpect(jsonPath("$.firstName").value(requestDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(requestDto.getLastName()))
                .andExpect(jsonPath("$.birthDate").value(requestDto.getBirthDate()))
                .andExpect(jsonPath("$.address").value(requestDto.getAddress()))
                .andExpect(jsonPath("$.phoneNumber").value(requestDto.getPhoneNumber()));
    }

    @Test
    @DisplayName("Update user contact info by id")
    public void updateUserContactInfo_ValidIdAndRequest_ReturnJsonUpdatedUser() throws Exception {
        // given
        int userId = 1;
        UpdateUserContactInfoRequestDto requestDto = new UpdateUserContactInfoRequestDto()
                .setAddress("UpdatedAddress")
                .setPhoneNumber("0987654321");

        UserDto updatedUserDto = new UserDto()
                .setUserId(userId)
                .setAddress(requestDto.getAddress())
                .setPhoneNumber(requestDto.getPhoneNumber());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        given(userService.updateUserContactInfo(
                eq(userId), any(UpdateUserContactInfoRequestDto.class))).willReturn(updatedUserDto);

        // when + then
        mockMvc.perform(patch("/api/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.address").value(requestDto.getAddress()))
                .andExpect(jsonPath("$.phoneNumber").value(requestDto.getPhoneNumber()));
    }

    @Test
    @DisplayName("Delete user by id")
    public void deleteUserById_ValidId_StatusNoContent() throws Exception {
        // given
        int userId = 1;

        doNothing().when(userService).deleteUserById(anyInt());

        // when + then
        mockMvc.perform(delete("/api/users/{userId}", userId))
                .andExpect(status().isNoContent());
    }
}
