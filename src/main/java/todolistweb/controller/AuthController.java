package todolistweb.controller;

import todolistweb.dto.UserRegistrationDto;
import todolistweb.model.User;
import todolistweb.repository.UserRepository;
import todolistweb.service.EmailService;
import todolistweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

@Controller
public class AuthController {

	private final UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private EmailService emailService;

	@Autowired
	public AuthController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	// Registrierung
	@PostMapping("/register")
	public String registerUser(@ModelAttribute("registrationDto") @Valid UserRegistrationDto userRegistrationDto,
			BindingResult bindingResult, Model model) {

		// Benutzername bereits vergeben
		if (userRepository.findByUsername(userRegistrationDto.getUsername()).isPresent()) {
			bindingResult.rejectValue("username", "error.username", "Benutzername existiert bereits!");
		}

		// E-Mail bereits vergeben
		if (userRepository.findByEmail(userRegistrationDto.getEmail()).isPresent()) {
			bindingResult.rejectValue("email", "error.email", "E-Mail existiert bereits!");
		}

		// Passwörter stimmen nicht überein – optional, wenn eigene Logik bevorzugt wird
		if (!userRegistrationDto.getPassword().equals(userRegistrationDto.getConfirmPassword())) {
			bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "Passwörter stimmen nicht überein");
		}

		// Validierungsfehler vorhanden
		if (bindingResult.hasErrors()) {
			model.addAttribute("registrationDto", userRegistrationDto);
			return "auth/register";
		}

		// Benutzer erstellen
		User user = new User();
		user.setUsername(userRegistrationDto.getUsername());
		user.setEmail(userRegistrationDto.getEmail());
		user.setPassword(userRegistrationDto.getPassword());

		try {
			String message = userService.registerUser(user);
			model.addAttribute("message", message);

			// E-Mail versenden
			emailService.sendVerificationEmail(user.getEmail(), user.getUsername(), user.getVerificationToken(),
					user.getTokenExpiryDate());

		} catch (Exception e) {
			bindingResult.rejectValue("email", "error.email",
					"Dieser Benutzername oder diese E-Mail existiert bereits.");
			model.addAttribute("registrationDto", userRegistrationDto);
			return "auth/register";
		}

		return "redirect:/auth/register?success";
	}

	// Login-Seite
	@GetMapping("/login")
	public String showLoginPage() {
		return "login"; // lädt login.html aus src/main/resources/templates/
	}

	// Registrierung
	@GetMapping("/auth/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("registrationDto", new UserRegistrationDto());
		return "auth/register"; // lädt register.html
	}

	// E-Mail-Verifizierung
	@GetMapping("/verify")
	public String verifyUser(@RequestParam("token") String token, Model model) {
		String message = userService.verifyEmail(token);
		model.addAttribute("message", message);
		return "auth/verify-success"; // neue Seite
	}
}
