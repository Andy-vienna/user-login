package todolistweb.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import todolistweb.model.Comment;
import todolistweb.model.Todo;
import todolistweb.model.User;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Gibt alle Kommentare zu einem ToDo zurück, chronologisch sortiert
    List<Comment> findByTodoOrderByCreatedAtAsc(Todo todo);
    
    @Query("SELECT MAX(c.createdAt) FROM Comment c WHERE c.todo.owner = :user OR :user MEMBER OF c.todo.sharedWith")
    	LocalDateTime findLatestCommentTimeForUser(@Param("user") User user);
}

