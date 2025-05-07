package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
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
                    List<String> lastLines = allLines.stream()
                            .skip(Math.max(0, allLines.size() - 50)) // nur die letzten 50 Zeilen
                            .collect(Collectors.toList());
                    model.addAttribute("logs", String.join("\n", lastLines));
                } else {
                    model.addAttribute("logs", "Keine Logdatei gefunden.");
                }
            } catch (IOException e) {
                model.addAttribute("logs", "Fehler beim Lesen der Logdatei: " + e.getMessage());
            }
        }

        return "home";
    }
    
    @GetMapping("/admin/logs")
    @ResponseBody
    public String getLogs() {
        try {
            Path logPath = Paths.get("logs/application.log");
            if (Files.exists(logPath)) {
                List<String> allLines = Files.readAllLines(logPath);
                List<String> lastLines = allLines.stream()
                        .skip(Math.max(0, allLines.size() - 50))
                        .collect(Collectors.toList());
                return String.join("\n", lastLines);
            } else {
                return "Keine Logdatei gefunden.";
            }
        } catch (IOException e) {
            return "Fehler beim Lesen der Logdatei: " + e.getMessage();
        }
    }
    
}
