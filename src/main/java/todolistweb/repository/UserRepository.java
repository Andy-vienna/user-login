package todolistweb.repository;

import java.util.Optional;

import todolistweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/** UserRepository ist ein Interface, das die CRUD-Operationen für die User-Entität definiert.
 *  *  @author Andreas Fischer
 *  *  @version 1.0
 *  *  @since 2025-05-10
 *  *  @Repository ist eine Annotation, die angibt, dass dieses Interface ein Repository ist.
 *  *  @SpringBootApplication ist eine Annotation, die angibt, dass dies die Hauptanwendungsklasse ist.
 *  *  *  @EnableJpaRepositories ist eine Annotation, die angibt, dass JPA-Repositories aktiviert sind.
 *  *  *  @EntityScan ist eine Annotation, die angibt, dass die Entitäten in diesem Paket gesucht werden sollen.
 */
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	Optional<User> findByResetToken(String token);

	Optional<User> findByUsername(String username);

	Optional<User> findByVerificationToken(String token);
}
