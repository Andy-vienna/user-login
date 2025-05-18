package todolistweb.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/** E-Mail Serviceklasse für den Versand von E-Mails.
 *  die Methoden befüllen E-Mail-Templates mit den entsprechenden Daten und versenden die E-Mails.
 */
@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private TemplateEngine templateEngine;

	/** Reset-Passwort Mail
	 * @param to
	 * @param username
	 * @param token
	 * @param expiryDateTime
	 * @throws MessagingException
	 */
	public void sendResetPassword(String to, String username, String token, LocalDateTime expiryDateTime) throws MessagingException {
		String subject = "Passwort zurücksetzen";
		String confirmationUrl = "http://localhost:8080/reset-password?token=" + token;

		Context context = new Context();
		context.setVariable("username", username);
		context.setVariable("confirmationUrl", confirmationUrl);
		context.setVariable("expiryDate",
				expiryDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").withLocale(Locale.GERMAN)));

		String htmlContent = templateEngine.process("auth/mail-template/password-reset", context);

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(htmlContent, true);

		mailSender.send(message);
	}

	/** Neu-User Verification Mail
	 * @param to
	 * @param username
	 * @param token
	 * @param expiryDateTime
	 * @throws MessagingException
	 */
	public void sendVerificationEmail(String to, String username, String token, LocalDateTime expiryDateTime) throws MessagingException {
		String subject = "Bitte bestätige deine E-Mail-Adresse";
		String confirmationUrl = "http://localhost:8080/verify?token=" + token;

		Context context = new Context();
		context.setVariable("username", username);
		context.setVariable("confirmationUrl", confirmationUrl);
		context.setVariable("expiryDate",
				expiryDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").withLocale(Locale.GERMAN)));

		String htmlContent = templateEngine.process("auth/mail-template/verify-register", context);

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(htmlContent, true);

		mailSender.send(message);
	}
	
	public void sendInvitationEmail(String recipient, String todo) throws MessagingException {
		String subject = "ToDoList-Web: Einladung zur Zusammenarbeit";
		String confirmationUrl = "http://localhost:8080/auth/register";

		Context context = new Context();
		context.setVariable("username", recipient);
		context.setVariable("confirmationUrl", confirmationUrl);

		String htmlContent = templateEngine.process("auth/mail-template/invitation-email", context);

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		helper.setTo(recipient);
		helper.setSubject(subject);
		helper.setText(htmlContent, true);

		mailSender.send(message);
	}
	
	public void sendShareNotification(String to, String recipient, String todo) throws MessagingException {
		String subject = "ToDoList-Web: ToDo wurde freigegeben";
		String confirmationUrl = "http://localhost:8080/login";

		Context context = new Context();
		context.setVariable("username", to);
		context.setVariable("confirmationUrl", confirmationUrl);

		String htmlContent = templateEngine.process("auth/mail-template/share-notification", context);

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		helper.setTo(recipient);
		helper.setSubject(subject);
		helper.setText(htmlContent, true);

		mailSender.send(message);
	}

}
