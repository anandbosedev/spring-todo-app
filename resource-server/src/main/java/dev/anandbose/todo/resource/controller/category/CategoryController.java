package dev.anandbose.todo.resource.controller.category;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import dev.anandbose.todo.resource.data.CategoryEntity;
import dev.anandbose.todo.resource.data.CategoryRepository;

import java.net.URI;
import java.security.Principal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getById(@PathVariable Long id, Principal principal) {
        return categoryRepository.findByIdAndOwner(id, principal.getName())
                .map(entity -> ResponseEntity.ok(CategoryDto.fromCategoryEntity(entity)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Transactional
    public ResponseEntity<List<CategoryDto>> getAll(Principal principal) {
        return ResponseEntity.ok(
                categoryRepository.findByOwner(principal.getName())
                        .map(CategoryDto::fromCategoryEntity)
                        .collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CategoryRequest request, Principal principal,
            UriComponentsBuilder ucb) {
        Date now = Calendar.getInstance().getTime();
        CategoryEntity entity = new CategoryEntity();
        entity.setName(request.name());
        entity.setCreatedOn(now);
        entity.setUpdatedOn(now);
        entity.setOwner(principal.getName());
        entity = categoryRepository.save(entity);
        URI location = ucb.path("/{id}").buildAndExpand(entity.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody CategoryRequest request,
            Principal principal) {
        Optional<CategoryEntity> result = categoryRepository.findByIdAndOwner(id, principal.getName());
        if (!result.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        CategoryEntity entity = result.get();
        entity.setName(request.name());
        categoryRepository.save(entity);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Principal principal) {
        Optional<CategoryEntity> result = categoryRepository.findByIdAndOwner(id, principal.getName());
        if (!result.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        CategoryEntity entity = result.get();
        entity.setDeletedOn(Calendar.getInstance().getTime());
        categoryRepository.save(entity);
        return ResponseEntity.noContent().build();
    }
}
