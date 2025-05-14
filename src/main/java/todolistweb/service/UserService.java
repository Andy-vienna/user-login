package todolistweb.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import todolistweb.model.User;
import todolistweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/** UserService ist eine Service-Klasse, die für die Registrierung und
 *  Verifizierung von Benutzern verantwortlich ist.
 *  *  Diese Klasse enthält Methoden zur Registrierung eines neuen Benutzers,
 *  *  zur Überprüfung der E-Mail-Adresse und zur Aktivierung des Benutzerkontos.
 *  *  @author Andreas Fischer
 *  *  @version 1.0
 *  *  @since 2025-05-12
 *  *  Diese Klasse ist Teil des ToDoListWeb-Projekts, das eine Webanwendung
 *  *  zur Verwaltung von Aufgaben und Projekten bereitstellt.
 */
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// Methode zur Registrierung des Users
	/**
	 * @param user
	 * @return
	 */
	public String registerUser(User user) {
		// Existenz prüfen
		if (userRepository.findByUsername(user.getUsername()).isPresent()) {
			return "Benutzername existiert bereits!";
		}

		if (userRepository.findByEmail(user.getEmail()).isPresent()) {
			return "E-Mail existiert bereits!";
		}

		// Passwort verschlüsseln
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		// Rolle setzen
		user.setRoles("ROLE_USER");

		// Benutzer zunächst deaktivieren
		user.setEnabled(false);

		// Token erzeugen
		String verificationToken = UUID.randomUUID().toString();
		user.setVerificationToken(verificationToken);
		user.setTokenExpiryDate(LocalDateTime.now().plusHours(24));

		// Speichern
		userRepository.save(user);

		return "Registrierung erfolgreich. Bitte überprüfe deine E-Mail zur Bestätigung.";
	}

	// Verifizierung des Tokens und Aktivierung des Accounts
	/**
	 * @param token
	 * @return
	 */
	public String verifyEmail(String token) {
		Optional<User> userOptional = userRepository.findByVerificationToken(token);

		if (userOptional.isEmpty()) {
			return "Ungültiger oder abgelaufener Bestätigungslink.";
		}

		if (userOptional.isPresent()) {
			User user = userOptional.get();

			if (user.getTokenExpiryDate() != null && user.getTokenExpiryDate().isBefore(LocalDateTime.now())) {
				return "Der Verifizierungslink ist abgelaufen.<br>Bitte registriere dich erneut.";
			}

			user.setEnabled(true);
			user.setVerificationToken(null); // Token nach der Bestätigung löschen
			user.setTokenExpiryDate(null); // Token-Ablaufdatum löschen
			userRepository.save(user);
			return "E-Mail erfolgreich bestätigt!<br>Du kannst dich nun anmelden...";
		}

		return "Ungültiger oder abgelaufener Token!";
	}
}
