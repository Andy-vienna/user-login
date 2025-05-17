package todolistweb.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/** Spring Security Konfiguration für die Webanwendung.
 *  *  @author Andreas Fischer
 *  *  @version 1.0
 *  *  @since 2025-05-10
 *  *  @Configuration ist eine Annotation, die angibt, dass diese Klasse eine Konfigurationsklasse ist.
 *  *  *  @EnableWebSecurity ist eine Annotation, die angibt, dass die Web-Sicherheit aktiviert ist.
 *  *  *  @Autowired ist eine Annotation, die angibt, dass die Abhängigkeit automatisch injiziert werden soll.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private final UserDetailsService userDetailsService;

	public SecurityConfig(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
		authBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
		return authBuilder.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(
						auth -> auth
								.requestMatchers("/register", "/login", "/verify", "/reset-request", "/reset-password",
										"/todos/**", "/auth/**", "/css/**", "/js/**", "/images/**")
								.permitAll().anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/login").failureUrl("/login?error").defaultSuccessUrl("/", true)
						.permitAll())
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login?logout").permitAll());

		return http.build();
	}
}
