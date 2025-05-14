package todolistweb.controller;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import todolistweb.model.User;
import todolistweb.repository.UserRepository;
import todolistweb.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.mail.MessagingException;
import jakarta.validation.constraints.NotEmpty;

/** PasswordController
 *  Controller for handling password reset functionality.
 *  Provides endpoints for requesting a password reset,
 *  validating the reset token,
 *  and updating the password.
 */
@Controller
public class PasswordResetController {

	public static class ResetPasswordForm {
		@NotEmpty
		private String newPassword;

		@NotEmpty
		private String confirmPassword;

		public String getConfirmPassword() {
			return confirmPassword;
		}

		public String getNewPassword() {
			return newPassword;
		}

		public void setConfirmPassword(String confirmPassword) {
			this.confirmPassword = confirmPassword;
		}

		public void setNewPassword(String newPassword) {
			this.newPassword = newPassword;
		}
	}

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private EmailService emailService;

	@PostMapping("/reset-password")
	public String handleResetPassword(@RequestParam("token") String token,
			@ModelAttribute("form") ResetPasswordForm form, BindingResult result, Model model) {
		Optional<User> optionalUser = userRepository.findByResetToken(token);

		if (optionalUser.isEmpty() || optionalUser.get().getResetTokenExpiry().isBefore(LocalDateTime.now())) {
			model.addAttribute("expired", true);
			return "auth/reset-password";
		}

		if (!form.getNewPassword().equals(form.getConfirmPassword())) {
			result.rejectValue("confirmPassword", "error.confirmPassword", "Passwörter stimmen nicht überein.");
			model.addAttribute("token", token);
			return "auth/reset-password";
		}

		User user = optionalUser.get();
		user.setPassword(passwordEncoder.encode(form.getNewPassword()));
		user.setResetToken(null);
		user.setResetTokenExpiry(null);
		userRepository.save(user);

		return "redirect:/auth/login?reset";
	}

	@PostMapping("/auth/reset-request")
	public String processForgotPassword(@RequestParam("email") String email, Model model) {
		Optional<User> optionalUser = userRepository.findByEmail(email);

		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			String token = UUID.randomUUID().toString();
			user.setResetToken(token);
			user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
			userRepository.save(user);

			try {
				emailService.sendResetPassword(user.getEmail(), user.getUsername(), token, user.getResetTokenExpiry());
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}

		model.addAttribute("emailSent", true);
		return "auth/reset-request";
	}

	@GetMapping("/auth/reset-request")
	public String showForgotPasswordForm(Model model) {
		model.addAttribute("email", "");
		return "auth/reset-request";
	}

	@GetMapping("/reset-password")
	public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
		System.out.println("→ /reset-password GET mit token = " + token);
		Optional<User> optionalUser = userRepository.findByResetToken(token);

		if (optionalUser.isEmpty() || optionalUser.get().getResetTokenExpiry().isBefore(LocalDateTime.now())) {
			model.addAttribute("expired", true);
			return "auth/reset-password";
		}

		model.addAttribute("expired", false); // Immer setzen, wenn gültig
		model.addAttribute("token", token);
		model.addAttribute("form", new ResetPasswordForm());
		return "auth/reset-password";
	}
}
