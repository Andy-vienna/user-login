package todolistweb.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import todolistweb.model.Comment;
import todolistweb.model.Todo;
import todolistweb.model.User;
import todolistweb.repository.CommentRepository;
import todolistweb.repository.TodoRepository;
import todolistweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** ToDoController
 *  Controller for managing ToDo items.
 *  Provides endpoints for listing, adding, completing,
 *  and deleting ToDo items.
 */
@Controller
public class TodoController {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    
    @Autowired
    public TodoController(TodoRepository todoRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @PostMapping("/todos")
    public String addTodo(@ModelAttribute("newTodo") Todo todo, Principal principal) {
    	User user = userRepository.findByUsername(principal.getName())
    	        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        todo.setUser(user);
        todoRepository.save(todo);
        return "redirect:/";
    }

    @PostMapping("/todos/{id}/complete")
    public String completeTodo(@PathVariable Long id,
                               @RequestParam(required = false) String comment,
                               Principal principal) {
        Todo todo = todoRepository.findById(id).orElseThrow();
        if (todo.getUser().getUsername().equals(principal.getName()) && !todo.isCompleted()) {
            todo.setCompleted(true);
            todo.setCompletedAt(LocalDateTime.now());
            if (comment != null) {
                todo.setComment(comment);
            }
            todoRepository.save(todo);
        }
        return "redirect:/";
    }
    
    @PostMapping("/todos/{id}/delete")
    public String deleteTodo(@PathVariable Long id, Principal principal) {
        Todo todo = todoRepository.findById(id).orElseThrow();

        // Sicherheit: Nur eigene Todos löschen
        if (todo.getUser().getUsername().equals(principal.getName())) {
            todoRepository.delete(todo);
        }

        return "redirect:/";
    }

    @PostMapping("/todos/{id}/comment")
    public String addComment(@PathVariable Long id,
                             @RequestParam String content,
                             Principal principal) {
        Todo todo = todoRepository.findById(id).orElseThrow();
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setAuthor(user);
        comment.setTodo(todo);

        commentRepository.save(comment);

        return "redirect:/";
    }

}