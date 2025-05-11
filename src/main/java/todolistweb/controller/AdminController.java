package todolistweb.controller;

import java.util.Optional;

import todolistweb.model.User;
import todolistweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminController {

	private final UserRepository userRepository;

	@Autowired
	public AdminController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@PostMapping("/admin/user/{id}/delete")
	@PreAuthorize("hasRole('ADMIN')")
	public String deleteUser(@PathVariable Long id) {
		userRepository.deleteById(id);
		return "redirect:/";
	}

	@PostMapping("/admin/user/{id}/disable")
	@PreAuthorize("hasRole('ADMIN')")
	public String disableUser(@PathVariable Long id) {
		userRepository.findById(id).ifPresent(user -> {
			user.setEnabled(false);
			userRepository.save(user);
		});
		return "redirect:/"; // zurück zur Übersicht
	}

	@PostMapping("/admin/user/{id}/enable")
	@PreAuthorize("hasRole('ADMIN')")
	public String enableUser(@PathVariable Long id) {
		userRepository.findById(id).ifPresent(user -> {
			user.setEnabled(true);
			userRepository.save(user);
		});
		return "redirect:/";
	}

	@PostMapping("/admin/user/{id}/toggle-role")
	public String toggleUserRole(@PathVariable Long id, @AuthenticationPrincipal UserDetails currentUser) {
		Optional<User> optionalUser = userRepository.findById(id);

		if (optionalUser.isEmpty()) {
			return "redirect:/";
		}

		User targetUser = optionalUser.get();

		// Selbstschutz: Rolle nicht für sich selbst ändern
		if (targetUser.getUsername().equals(currentUser.getUsername())) {
			return "redirect:/";
		}

		if (targetUser.getRoles().contains("ADMIN")) {
			targetUser.setRoles("ROLE_USER");
		} else {
			targetUser.setRoles("ROLE_ADMIN");
		}

		userRepository.save(targetUser);
		return "redirect:/";
	}

}
