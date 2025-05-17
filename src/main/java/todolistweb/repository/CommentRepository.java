package todolistweb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import todolistweb.model.Comment;
import todolistweb.model.Todo;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Gibt alle Kommentare zu einem ToDo zurück, chronologisch sortiert
    List<Comment> findByTodoOrderByCreatedAtAsc(Todo todo);
}

