package todolistweb.repository;

import java.util.Optional;

import todolistweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	Optional<User> findByResetToken(String token);

	Optional<User> findByUsername(String username);

	Optional<User> findByVerificationToken(String token);
}
