package todolistweb.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;

/** DataModel for the Todo entity.
 *  * @author Andreas Fischer
 *  * @version 1.0
 *  * @since 2025-05-10
 *  This class represents a Todo item in the application.
 *  It contains fields for the Todo's ID, description, completion status,
 *  associated user, creation time, completion time, and comments.
 *  It also includes getter and setter methods for each field.
 *  *  The @Entity annotation indicates that this class is a JPA entity,
 *  *  and the @Id annotation specifies the primary key of the entity.
 *  *  The @GeneratedValue annotation specifies the strategy for generating
 *  *  the primary key value.
 *  *  The @ManyToOne annotation indicates a many-to-one relationship with the User entity,
 *  *  and the @JoinColumn annotation specifies the foreign key column in the database.
 *  *  The @PrePersist annotation is used to set the creation time of the Todo item
 *  *  before it is persisted to the database.
 *  *  The class also includes a field for the completion time and a field for comments,
 *  *  *  with a maximum length of 500 characters.
 *  *  The class is part of the todolistweb.model package.
 *  *  @Entity
 *  *  @Table(name = "todos")
 *  *  @GeneratedValue(strategy = GenerationType.IDENTITY)
 *  *  @Id
 *  *  @Column(name = "user_id")
 *  *  @ManyToOne
 *  *  @JoinColumn(name = "user_id")
 *  *  @PrePersist
 *  *  @Column(nullable = false, updatable = false)
 *  *  @Column(length = 500)
 *  *  @ManyToOne
 */
@Entity
public class Todo {
	
	@PrePersist
	protected void onCreate() {
	    this.createdAt = LocalDateTime.now();
	}
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	private String description;
    private boolean completed;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

	private LocalDateTime completedAt;
	
	@Column(length = 500)
	private String comment;
	
	@OneToMany(mappedBy = "todo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<Comment> comments = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "owner_id")
	private User owner;

	@ManyToMany
	@JoinTable(
	    name = "todo_shared_with",
	    joinColumns = @JoinColumn(name = "todo_id"),
	    inverseJoinColumns = @JoinColumn(name = "user_id")
	)
	private Set<User> sharedWith = new HashSet<>();

	@Column(name = "is_collaborative")
	private boolean collaborative = false;

    // Getter/Setter

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getCompletedAt() {
		return completedAt;
	}

	public void setCompletedAt(LocalDateTime completedAt) {
		this.completedAt = completedAt;
	}
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public List<Comment> getComments() {
	    return comments;
	}

	public void setComments(List<Comment> comments) {
	    this.comments = comments;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Set<User> getSharedWith() {
		return sharedWith;
	}

	public void setSharedWith(Set<User> sharedWith) {
		this.sharedWith = sharedWith;
	}

	public boolean isCollaborative() {
		return collaborative;
	}

	public void setCollaborative(boolean collaborative) {
		this.collaborative = collaborative;
	}
	
}
