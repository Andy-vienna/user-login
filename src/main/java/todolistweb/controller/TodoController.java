package todolistweb.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import todolistweb.model.Comment;
import todolistweb.model.Todo;
import todolistweb.model.User;
import todolistweb.repository.CommentRepository;
import todolistweb.repository.TodoRepository;
import todolistweb.repository.UserRepository;
import todolistweb.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.mail.MessagingException;

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
	private EmailService emailService;
    
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
        todo.setOwner(user);
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
    
    @PostMapping("/todos/share")
    public String shareToDo(@RequestParam Long todoId, @RequestParam String identifier, Principal principal, RedirectAttributes redirectAttributes) {
        User owner = userRepository.findByUsername(principal.getName())
            .orElseThrow(() -> new UsernameNotFoundException("Benutzer nicht gefunden"));

        Todo todo = todoRepository.findByIdWithShared(todoId)
        	    .orElseThrow(() -> new IllegalArgumentException("ToDo nicht gefunden"));

        if (!todo.getOwner().getId().equals(owner.getId())) {
            throw new AccessDeniedException("Nicht der Besitzer der Aufgabe");
        }

        Optional<User> recipientOpt;
        if (identifier.contains("@")) {
            recipientOpt = userRepository.findByEmail(identifier);
        } else {
            recipientOpt = userRepository.findByUsername(identifier);
        }

        if (recipientOpt.isEmpty() || !recipientOpt.get().isEnabled()) {
            redirectAttributes.addFlashAttribute("shareError", "Der Benutzer ist nicht registriert oder noch nicht aktiviert.");
            return "redirect:/";
        }
        
        User recipient = recipientOpt.get();
        
        if (todo.getSharedWith().contains(recipient)) {
            redirectAttributes.addFlashAttribute("shareError", "Die Aufgabe ist bereits für diesen Benutzer freigegeben.");
            return "redirect:/";
        }

        todo.getSharedWith().add(recipient);
        todoRepository.save(todo);
        
        Comment systemComment = new Comment();
        systemComment.setTodo(todo);
        systemComment.setAuthor(owner);
        systemComment.setCreatedAt(LocalDateTime.now());
        systemComment.setContent("Freigabe für " + recipient.getUsername() + " erteilt");

        commentRepository.save(systemComment);

        String to = recipient.getUsername();
        String mail = recipient.getEmail();
        String txtTodo = todo.getDescription();

        try {
            emailService.sendShareNotification(to, mail, txtTodo);
        } catch (MessagingException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("shareError", "Fehler beim Senden der E-Mail.");
        }

        redirectAttributes.addFlashAttribute("shareSuccess", "Aufgabe wurde erfolgreich freigegeben.");
        return "redirect:/";
    }

    @GetMapping("/todo/last-update")
    @ResponseBody
    public String getLastUpdate(Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
            .orElseThrow();
        LocalDateTime last = commentRepository.findLatestCommentTimeForUser(user);
        return last != null ? last.toString() : "";
    }


}