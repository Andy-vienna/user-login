package todolistweb.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;
	private String password;
	private String roles;
	private boolean enabled;
	private String email;
	private String phoneNumber;
	private String verificationToken;
	private LocalDateTime tokenExpiryDate;
	private String resetToken;
	private LocalDateTime resetTokenExpiry;

	public User() {
	}

	public User(String username, String password, String roles, boolean enabled, String email,
			String verificationToken) {
		this.username = username;
		this.password = password;
		this.roles = roles;
		this.enabled = enabled;
		this.email = email;
		this.verificationToken = verificationToken;
		this.tokenExpiryDate = LocalDateTime.now().plusHours(24); // Token 24h gültig
	}

	public String getEmail() {
		return email;
	}

	// Getter und Setter
	public Long getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getResetToken() {
		return resetToken;
	}

	public LocalDateTime getResetTokenExpiry() {
		return resetTokenExpiry;
	}

	public String getRoles() {
		return roles;
	}

	public LocalDateTime getTokenExpiryDate() {
		return tokenExpiryDate;
	}

	public String getUsername() {
		return username;
	}

	public String getVerificationToken() {
		return verificationToken;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}

	public void setResetTokenExpiry(LocalDateTime resetTokenExpiry) {
		this.resetTokenExpiry = resetTokenExpiry;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public void setTokenExpiryDate(LocalDateTime tokenExpiryDate) {
		this.tokenExpiryDate = tokenExpiryDate;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setVerificationToken(String verificationToken) {
		this.verificationToken = verificationToken;
	}

}
