package todolistweb.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/** PasswordValidation
 *  Passwort-Prüfer um zu prüfen, ob die eingegebenen Passwörter übereinstimmen.
 */
@Documented
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatches {
	Class<?>[] groups() default {};

	String message() default "Passwörter stimmen nicht überein";

	Class<? extends Payload>[] payload() default {};
}
