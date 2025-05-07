package com.example.demo.dto;

import com.example.demo.validation.PasswordMatches;
import jakarta.validation.constraints.*;

@PasswordMatches
public class UserRegistrationDto {

	@NotEmpty(message = "Benutzername darf nicht leer sein")
	private String username;
	
	@NotEmpty(message = "E-Mail darf nicht leer sein")
	private String email;

	@NotEmpty(message = "Passwort darf nicht leer sein")
	@Size(min = 8, message = "Passwort muss mindestens 8 Zeichen lang sein")
	@Pattern(
	    regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&_\\-])[A-Za-z\\d@$!%*?&_\\-]{8,}$",
	    message = "Passwort muss Großbuchstaben, Kleinbuchstaben, Zahl und Sonderzeichen (@$!%*?&_-) enthalten"
	)
	private String password;

	@NotEmpty(message = "Passwort-Bestätigung darf nicht leer sein")
	private String confirmPassword;

	// Getter/Setter oder Lombok @Data

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}
