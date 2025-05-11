package todolistweb.service;

import todolistweb.model.User;
import todolistweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	@Autowired
	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * Lädt Benutzerdaten basierend auf dem Benutzernamen.
	 * 
	 * @param username Der Benutzername des zu ladenden Benutzers
	 * @return UserDetails, das Benutzerdaten enthält
	 * @throws UsernameNotFoundException Wenn der Benutzer nicht gefunden wird
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(user.getUsername());
		builder.password(user.getPassword());
		builder.roles(user.getRoles().replace("ROLE_", "")); // Entfernt "ROLE_" für die Spring Security-Rollen
		builder.disabled(!user.isEnabled()); // Verhindert Login, wenn nicht verifiziert

		return builder.build();
	}
}
