package sosunovych.user.registration.dto;

import lombok.Data;

@Data
public class UpdateUserContactInfoRequestDto {
    private String address;
    private String phoneNumber;
}
