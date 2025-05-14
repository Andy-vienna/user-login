package todolistweb.repository;

import java.time.LocalDateTime;
import java.util.List;

import todolistweb.model.Todo;
import todolistweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/** TodoRepository ist ein Interface, das die CRUD-Operationen für die Todo-Entität definiert.
 *  *  @author Andreas Fischer
 *  *  @version 1.0
 *  *  @since 2025-05-10
 *  *  @Repository ist eine Annotation, die angibt, dass dieses Interface ein Repository ist.
 */
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByUser(User user);
    
    @Query("SELECT t FROM Todo t WHERE t.user = :user AND " +
    	       "(t.completed = false OR t.completedAt >= :cutoff)")
    	List<Todo> findVisibleTodosByUser(@Param("user") User user,
    	                                   @Param("cutoff") LocalDateTime cutoff);

}
