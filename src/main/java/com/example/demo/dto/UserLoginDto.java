package com.example.demo.dto;

import jakarta.validation.constraints.NotEmpty;

public class UserLoginDto {
	
	@NotEmpty(message = "Benutzername darf nicht leer sein")
	private String username;
    
	@NotEmpty(message = "Passwort darf nicht leer sein")
    private String password;

    // Getter und Setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
