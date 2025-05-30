package todolistweb.validation;

import todolistweb.dto.UserRegistrationDto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/** PasswordMatchingValidator
 *  Prüfer um zu prüfen, ob die eingegebenen Passwörter übereinstimmen.
 */
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserRegistrationDto> {

	@Override
	public boolean isValid(UserRegistrationDto dto, ConstraintValidatorContext context) {
		if (dto.getPassword() == null || dto.getConfirmPassword() == null) {
			return false;
		}
		return dto.getPassword().equals(dto.getConfirmPassword());
	}
}
