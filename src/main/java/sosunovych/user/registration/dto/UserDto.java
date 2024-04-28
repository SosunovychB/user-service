package sosunovych.user.registration.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class UserDto {
    private int userId;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;
}
