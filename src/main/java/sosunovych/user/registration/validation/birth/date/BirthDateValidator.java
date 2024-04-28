package sosunovych.user.registration.validation.birth.date;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Value;
import sosunovych.user.registration.exception.InvalidDateFormatException;

public class BirthDateValidator implements ConstraintValidator<BirthDate, String> {
    private static final String DATE_PATTERN = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";
    @Value("${minimum.age}")
    private int minimumAge;

    @Override
    public boolean isValid(String birthDate, ConstraintValidatorContext context) {
        if (birthDate == null || !Pattern.compile(DATE_PATTERN).matcher(birthDate).matches()) {
            throw new InvalidDateFormatException("Invalid date format or empty 'birthDate'."
                    + " It can not be empty and must be in format yyyy-MM-dd");
        }

        return LocalDate.parse(birthDate).until(LocalDate.now()).getYears() >= minimumAge;
    }
}
