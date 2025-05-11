package todolistweb.repository;

import java.time.LocalDateTime;
import java.util.List;

import todolistweb.model.Todo;
import todolistweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByUser(User user);
    
    @Query("SELECT t FROM Todo t WHERE t.user = :user AND " +
    	       "(t.completed = false OR t.completedAt >= :cutoff)")
    	List<Todo> findVisibleTodosByUser(@Param("user") User user,
    	                                   @Param("cutoff") LocalDateTime cutoff);

}
