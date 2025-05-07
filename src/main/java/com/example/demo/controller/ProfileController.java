package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String showProfile(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        User user = userRepository.findByUsername(currentUser.getUsername()).orElseThrow();
        model.addAttribute("user", user);
        return "profile/profile";
    }

    @PostMapping
    public String updateProfile(@ModelAttribute("user") User updatedUser,
                                @AuthenticationPrincipal UserDetails currentUser,
                                Model model) {
        User user = userRepository.findByUsername(currentUser.getUsername()).orElseThrow();
        user.setEmail(updatedUser.getEmail());
        user.setPhoneNumber(updatedUser.getPhoneNumber());
        userRepository.save(user);
        model.addAttribute("user", user);
        model.addAttribute("success", true);
        return "profile/profile";
    }

    @GetMapping("/password")
    public String showPasswordForm(Model model) {
        model.addAttribute("passwordForm", new PasswordForm());
        return "profile/change-password"; // → Template aufrufen
    }

    @PostMapping("/password")
    public String changePassword(@ModelAttribute("passwordForm") @Valid PasswordForm form,
                                 BindingResult result,
                                 @AuthenticationPrincipal UserDetails currentUser,
                                 Model model) {

        User user = userRepository.findByUsername(currentUser.getUsername()).orElseThrow();

        if (!passwordEncoder.matches(form.getCurrentPassword(), user.getPassword())) {
            result.rejectValue("currentPassword", "error.currentPassword", "Aktuelles Passwort ist falsch.");
        }

        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.confirmPassword", "Passwörter stimmen nicht überein.");
        }

        if (result.hasErrors()) {
            return "/profile/change-password";
        }

        user.setPassword(passwordEncoder.encode(form.getNewPassword()));
        userRepository.save(user);
        model.addAttribute("success", true);
        return "profile/change-password";
    }

    //@Data
    public static class PasswordForm {
        @NotEmpty
        private String currentPassword;

        @NotEmpty
        @Size(min = 8, message = "Passwort muss mindestens 8 Zeichen lang sein.")
        private String newPassword;

        @NotEmpty
        private String confirmPassword;

		public String getCurrentPassword() {
			return currentPassword;
		}

		public void setCurrentPassword(String currentPassword) {
			this.currentPassword = currentPassword;
		}

		public String getNewPassword() {
			return newPassword;
		}

		public void setNewPassword(String newPassword) {
			this.newPassword = newPassword;
		}

		public String getConfirmPassword() {
			return confirmPassword;
		}

		public void setConfirmPassword(String confirmPassword) {
			this.confirmPassword = confirmPassword;
		}
    }
}
