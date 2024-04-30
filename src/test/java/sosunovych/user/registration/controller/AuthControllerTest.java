package sosunovych.user.registration.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import sosunovych.user.registration.dto.RegisterUserRequestDto;
import sosunovych.user.registration.dto.UserDto;
import sosunovych.user.registration.service.UserService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
public class AuthControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

    @Test
    @DisplayName("Register new user")
    public void registerNewUser_ValidRequest_ReturnJsonUserDto() throws Exception {
        //given
        RegisterUserRequestDto requestDto = new RegisterUserRequestDto()
                .setEmail("test@example.com")
                .setFirstName("Name")
                .setLastName("LastName")
                .setBirthDate("2000-01-01")
                .setAddress("Address")
                .setPhoneNumber("1234567890");

        UserDto userDto = new UserDto()
                .setUserId(1)
                .setEmail(requestDto.getEmail())
                .setFirstName(requestDto.getFirstName())
                .setLastName(requestDto.getLastName())
                .setBirthDate(LocalDate.parse(requestDto.getBirthDate()))
                .setAddress(requestDto.getAddress())
                .setPhoneNumber(requestDto.getPhoneNumber());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        given(userService.registerNewUser(any(RegisterUserRequestDto.class))).willReturn(userDto);

        //when + then
        mvc.perform(MockMvcRequestBuilders.post("/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId")
                        .exists())
                .andExpect(jsonPath("$.email")
                        .value(userDto.getEmail()))
                .andExpect(jsonPath("$.firstName")
                        .value(userDto.getFirstName()))
                .andExpect(jsonPath("$.lastName")
                        .value(userDto.getLastName()))
                .andExpect(jsonPath("$.birthDate")
                        .value(String.valueOf(userDto.getBirthDate())))
                .andExpect(jsonPath("$.address")
                        .value(userDto.getAddress()))
                .andExpect(jsonPath("$.phoneNumber")
                        .value(userDto.getPhoneNumber()));
    }
}
