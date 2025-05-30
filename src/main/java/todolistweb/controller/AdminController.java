package todolistweb.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import todolistweb.model.Todo;
import todolistweb.model.User;
import todolistweb.repository.TodoRepository;
import todolistweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** AdminController
 *  Controller for managing users in the application.
 *  Provides endpoints for deleting, enabling, disabling users and toggling user roles.
 */
@Controller
public class AdminController {

	@Autowired
	public AdminController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Autowired
	private TodoRepository todoRepository;

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/admin/user/{id}/delete")
	public String deleteUser(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
	    User toDelete = userRepository.findById(id).orElse(null);
	    User currentUser = userRepository.findByUsername(principal.getName()).orElse(null);

	    if (toDelete == null || currentUser == null) {
	        redirectAttributes.addFlashAttribute("shareError", "Benutzer nicht gefunden.");
	        return "redirect:/home";
	    }

	    if (toDelete.getUsername().equals(currentUser.getUsername())) {
	        redirectAttributes.addFlashAttribute("shareError", "Du kannst dich nicht selbst löschen.");
	        return "redirect:/home";
	    }

	    // ✅ 1. ToDos des Benutzers → sharedWith leeren
	    List<Todo> ownedTodos = todoRepository.findByOwnerWithShares(toDelete);
	    for (Todo todo : ownedTodos) {
	        todo.getSharedWith().clear();
	    }

	    // ✅ 2. ToDos, für die er freigegeben wurde → ihn aus sharedWith entfernen
	    List<Todo> sharedWithTodos = todoRepository.findAllSharedWith(toDelete.getId());
	    for (Todo todo : sharedWithTodos) {
	        todo.getSharedWith().remove(toDelete);
	    }

	    // ✅ speichern
	    todoRepository.saveAll(ownedTodos);
	    todoRepository.saveAll(sharedWithTodos);

	    // ✅ Benutzer löschen
	    userRepository.delete(toDelete);

	    redirectAttributes.addFlashAttribute("shareSuccess", "Benutzer gelöscht – Freigaben entfernt.");
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
