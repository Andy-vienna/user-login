package com.example.demo.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendVerificationEmail(String to, String username, String token, LocalDateTime expiryDateTime) throws MessagingException {
        String subject = "Bitte bestätige deine E-Mail-Adresse";
        String confirmationUrl = "http://localhost:8080/verify?token=" + token;

        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("confirmationUrl", confirmationUrl);
        context.setVariable("expiryDate", expiryDateTime.format(
                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").withLocale(Locale.GERMAN)
        ));

        String htmlContent = templateEngine.process("auth/mail-template/verify-register", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
    
    public void sendResetPassword(String to, String username, String token, LocalDateTime expiryDateTime) throws MessagingException {
        String subject = "Passwort zurücksetzen";
        String confirmationUrl = "http://localhost:8080/reset-password?token=" + token;

        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("confirmationUrl", confirmationUrl);
        context.setVariable("expiryDate", expiryDateTime.format(
                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").withLocale(Locale.GERMAN)
        ));

        String htmlContent = templateEngine.process("auth/mail-template/password-reset", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
    
}
