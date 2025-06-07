package dev.anandbose.todo.resource.controller.todo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import dev.anandbose.todo.resource.data.CategoryEntity;
import dev.anandbose.todo.resource.data.CategoryRepository;
import dev.anandbose.todo.resource.data.TodoEntity;
import dev.anandbose.todo.resource.data.TodoRepository;
import dev.anandbose.todo.resource.data.TodoStatus;
import java.net.URI;
import java.security.Principal;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/todo")
public class TodoController {
    private final TodoRepository todoRepository;
    private final CategoryRepository categoryRepository;

    public TodoController(TodoRepository todoRepository, CategoryRepository categoryRepository) {
        this.todoRepository = todoRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public PagedModel<TodoDto> getAll(Principal principal, Pageable pageable) {
        return new PagedModel<>(todoRepository.findByOwner(principal.getName(), pageable).map(TodoDto::fromTodoEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoDto> getById(@PathVariable Long id, Principal principal) {
        return todoRepository.findByIdAndOwner(id, principal.getName())
                .map(TodoDto::fromTodoEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<PagedModel<TodoDto>> getAllByCategory(@PathVariable Long categoryId, Principal principal,
            Pageable pageable) {
        return categoryRepository.findByIdAndOwner(categoryId, principal.getName())
                .map(categoryEntity -> ResponseEntity.ok(new PagedModel<>(
                        todoRepository.findByOwnerAndCategory(principal.getName(), categoryEntity, pageable)
                                .map(TodoDto::fromTodoEntity))))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreateTodoRequest request, Principal principal,
            UriComponentsBuilder ucb) {
        String owner = principal.getName();
        Date now = Calendar.getInstance().getTime();
        Set<CategoryEntity> categories = categoryRepository.findByMultipleIdAndOwner(request.categories(), owner)
                .stream().collect(Collectors.toSet());
        TodoEntity entity = new TodoEntity();
        entity.setOwner(owner);
        entity.setStatus(TodoStatus.TODO);
        entity.setCreatedOn(now);
        entity.setUpdatedOn(now);
        entity.setDescription(request.description());
        entity.setCategories(categories);
        entity = todoRepository.save(entity);
        URI location = ucb.path("{id}").buildAndExpand(entity.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("{id}")
    public ResponseEntity<Void> updateTodo(@PathVariable Long id, @RequestBody UpdateTodoRequest request, Principal principal) {
        Optional<TodoEntity> result = todoRepository.findByIdAndOwner(id, principal.getName());
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        TodoEntity entity = result.get();
        if (request.description() != null) {
            entity.setDescription(request.description());
        }
        if (request.status() != null) {
            entity.setStatus(request.status());
        }
        if (request.description() != null || request.status() != null) {
            entity.setUpdatedOn(Calendar.getInstance().getTime());
            todoRepository.save(entity);
        }
        return ResponseEntity.noContent().build();
    }
}
