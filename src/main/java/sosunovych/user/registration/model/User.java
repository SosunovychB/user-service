package sosunovych.user.registration.model;

import lombok.Data;
import lombok.NonNull;
import sosunovych.user.registration.validation.Email;

import javax.validation.constraints.*;
import java.time.*;

@Data
public class User {
    @NonNull
    @Email
    private String email;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;
}
