package sosunovych.user.registration.mapper;

import java.time.LocalDate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import sosunovych.user.registration.config.MapperConfig;
import sosunovych.user.registration.dto.RegisterUserRequestDto;
import sosunovych.user.registration.dto.UserDto;
import sosunovych.user.registration.model.User;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    @Mapping(source = "birthDate", target = "birthDate", qualifiedByName = "stringToLocalDate")
    User registrationRequestToEntity(RegisterUserRequestDto registerUserRequestDto);

    UserDto entityToUserDto(User user);

    @Named("stringToLocalDate")
    default LocalDate stringToLocalDate(String birthDate) {
        return LocalDate.parse(birthDate);
    }
}
