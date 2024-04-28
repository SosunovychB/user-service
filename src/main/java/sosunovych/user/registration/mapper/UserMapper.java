package sosunovych.user.registration.mapper;

import java.time.LocalDate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import sosunovych.user.registration.config.MapperConfig;
import sosunovych.user.registration.dto.RegisterUserRequestDto;
import sosunovych.user.registration.dto.UpdateFullUserInfoRequestDto;
import sosunovych.user.registration.dto.UpdateUserContactInfoRequestDto;
import sosunovych.user.registration.dto.UserDto;
import sosunovych.user.registration.model.User;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    @Mapping(source = "birthDate", target = "birthDate", qualifiedByName = "stringToLocalDate")
    User registrationRequestToEntity(RegisterUserRequestDto registerUserRequestDto);

    UserDto entityToUserDto(User user);

    User updateFullUserInfo(@MappingTarget User user,
                            UpdateFullUserInfoRequestDto requestDto);

    @Mapping(target = "address",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "phoneNumber",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User updateUserContactInfo(@MappingTarget User user,
                               UpdateUserContactInfoRequestDto requestDto);

    @Named("stringToLocalDate")
    default LocalDate stringToLocalDate(String birthDate) {
        return LocalDate.parse(birthDate);
    }
}
