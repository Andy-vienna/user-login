package todolistweb.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/** Data Model for the reset password form.
 * @author Andreas Fischer
 * @version 1.0
 *  * @since 2025-05-10
 *  * @description This class represents the form used for resetting a password.
 *  * It contains fields for the token, new password, and confirmation password.
 *  * The class includes validation annotations to ensure that the fields are not empty
 *  * and that the new password meets the minimum length requirement.
 *  * The class is part of the todolistweb.model package.
 *  *  @NotEmpty
 *  *  @Size(min = 8, message = "Passwort muss mindestens 8 Zeichen lang sein.")
 *  *  @NotEmpty
 */
public class ResetForm {

	@NotEmpty
	private String token;

	@NotEmpty
	@Size(min = 8, message = "Passwort muss mindestens 8 Zeichen lang sein.")
	private String newPassword;

	@NotEmpty
	private String confirmPassword;

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	// Getter & Setter
	public String getToken() {
		return token;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
