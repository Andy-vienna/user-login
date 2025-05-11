package todolistweb.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

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
