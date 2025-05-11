package todolistweb.dto;

import jakarta.validation.constraints.NotEmpty;

public class UserLoginDto {

	@NotEmpty(message = "Benutzername darf nicht leer sein")
	private String username;

	@NotEmpty(message = "Passwort darf nicht leer sein")
	private String password;

	public String getPassword() {
		return password;
	}

	// Getter und Setter
	public String getUsername() {
		return username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
