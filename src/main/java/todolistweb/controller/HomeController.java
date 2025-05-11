package todolistweb.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import todolistweb.model.Todo;
import todolistweb.model.User;
import todolistweb.repository.TodoRepository;
import todolistweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TodoRepository todoRepository;

	@GetMapping("/admin/logs")
	@ResponseBody
	public String getLogs() {
		try {
			Path logPath = Paths.get("logs/application.log");
			if (Files.exists(logPath)) {
				List<String> allLines = Files.readAllLines(logPath);
				List<String> lastLines = allLines.stream().skip(Math.max(0, allLines.size() - 50))
						.collect(Collectors.toList());
				return String.join("\n", lastLines);
			} else {
				return "Keine Logdatei gefunden.";
			}
		} catch (IOException e) {
			return "Fehler beim Lesen der Logdatei: " + e.getMessage();
		}
	}

	@GetMapping("/")
	public String home(Model model, Authentication authentication, Principal principal) {
		boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

		if (isAdmin) {
			// Benutzerliste für Admin
			List<User> users = userRepository.findAll();
			model.addAttribute("users", users);

			// Logs einlesen
			try {
				Path logPath = Paths.get("logs/application.log");
				if (Files.exists(logPath)) {
					List<String> allLines = Files.readAllLines(logPath);
					List<String> lastLines = allLines.stream().skip(Math.max(0, allLines.size() - 50)) // nur die
																										// letzten 50
																										// Zeilen
							.collect(Collectors.toList());
					model.addAttribute("logs", String.join("\n", lastLines));
				} else {
					model.addAttribute("logs", "Keine Logdatei gefunden.");
				}
			} catch (IOException e) {
				model.addAttribute("logs", "Fehler beim Lesen der Logdatei: " + e.getMessage());
			}
		}
		
		if (!isAdmin) {
	        User user = userRepository.findByUsername(principal.getName())
	                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

	        LocalDateTime cutoff = LocalDateTime.now().minusDays(2);
	        List<Todo> visibleTodos = todoRepository.findVisibleTodosByUser(user, cutoff);

	        List<Todo> openTodos = visibleTodos.stream()
	                .filter(t -> !t.isCompleted())
	                .toList();

	        List<Todo> doneTodos = visibleTodos.stream()
	                .filter(Todo::isCompleted)
	                .toList();

	        model.addAttribute("openTodos", openTodos);
	        model.addAttribute("doneTodos", doneTodos);
	        model.addAttribute("newTodo", new Todo());
	    }

		return "home";
	}

}
