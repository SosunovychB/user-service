package sosunovych.user.registration.model;

import java.time.LocalDate;
import lombok.Data;

@Data
public class User {
    private int userId;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;
    private boolean isDeleted = false;
}
