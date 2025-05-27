package todolistweb.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/** DataModel for the user entity.
 *  * @author Andreas Fischer
 *  * @version 1.0
 *  * @since 2025-05-10
 *  * @description This class represents a user in the application.
 *  *  It contains fields for the user's ID, username, password, roles,
 *  *  *  enabled status, email, phone number, verification token,
 *  *  *  token expiry date, reset token, and reset token expiry date.
 *  *  *  The class includes getter and setter methods for each field.
 *  *  *  The @Entity annotation indicates that this class is a JPA entity,
 *  *  *  and the @Table annotation specifies the name of the database table.
 *  *  *  The @Id annotation specifies the primary key of the entity,
 *  *  *  and the @GeneratedValue annotation specifies the strategy for generating
 *  *  *  the primary key value.
 *  *  *  The class is part of the todolistweb.model package.
 *  *  *  @Entity
 *  *  *  @Table(name = "users")
 *  *  *  @GeneratedValue(strategy = GenerationType.IDENTITY)
 */
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
	
	@OneToMany(mappedBy = "owner")
	private List<Todo> ownedTodos;

	@ManyToMany(mappedBy = "sharedWith")
	private List<Todo> sharedTodos;

	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (!(o instanceof User)) return false;
	    User other = (User) o;
	    return id != null && id.equals(other.getId());
	}

	@Override
	public int hashCode() {
	    return Objects.hash(id);
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

	public List<Todo> getOwnedTodos() {
		return ownedTodos;
	}

	public void setOwnedTodos(List<Todo> ownedTodos) {
		this.ownedTodos = ownedTodos;
	}

	public List<Todo> getSharedTodos() {
		return sharedTodos;
	}

	public void setSharedTodos(List<Todo> sharedTodos) {
		this.sharedTodos = sharedTodos;
	}
	
	

}
