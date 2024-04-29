package sosunovych.user.registration.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateUserContactInfoRequestDto {
    private String address;
    private String phoneNumber;
}
