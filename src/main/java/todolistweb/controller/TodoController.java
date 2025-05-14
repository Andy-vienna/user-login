package todolistweb.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import todolistweb.model.Todo;
import todolistweb.model.User;
import todolistweb.repository.TodoRepository;
import todolistweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** ToDoController
 *  Controller for managing ToDo items.
 *  Provides endpoints for listing, adding, completing,
 *  and deleting ToDo items.
 */
@Controller
@RequestMapping("/todos")
public class TodoController {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    @Autowired
    public TodoController(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String listTodos(Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Todo> allTodos = todoRepository.findByUser(user);
        List<Todo> openTodos = allTodos.stream()
            .filter(t -> !t.isCompleted())
            .toList();
        List<Todo> doneTodos = allTodos.stream()
            .filter(t -> t.isCompleted())
            .toList();

        model.addAttribute("openTodos", openTodos);
        model.addAttribute("doneTodos", doneTodos);
        model.addAttribute("newTodo", new Todo());
        return "todos/list";
    }

    @PostMapping
    public String addTodo(@ModelAttribute("newTodo") Todo todo, Principal principal) {
    	User user = userRepository.findByUsername(principal.getName())
    	        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        todo.setUser(user);
        todoRepository.save(todo);
        return "redirect:/";
    }

    @PostMapping("/{id}/complete")
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
    
    @PostMapping("/{id}/delete")
    public String deleteTodo(@PathVariable Long id, Principal principal) {
        Todo todo = todoRepository.findById(id).orElseThrow();

        // Sicherheit: Nur eigene Todos löschen
        if (todo.getUser().getUsername().equals(principal.getName()) && todo.isCompleted()) {
            todoRepository.delete(todo);
        }

        return "redirect:/";
    }
    
}

