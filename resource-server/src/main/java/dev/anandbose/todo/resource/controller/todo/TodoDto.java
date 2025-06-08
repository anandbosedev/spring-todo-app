package dev.anandbose.todo.resource.controller.todo;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import dev.anandbose.todo.resource.controller.category.CategoryDto;
import dev.anandbose.todo.resource.data.TodoEntity;
import dev.anandbose.todo.resource.data.TodoStatus;

public record TodoDto(Long id, String description, TodoStatus status, Set<CategoryDto> categories, Date createdOn,
                Date updatedOn) {
        public static TodoDto fromTodoEntity(TodoEntity entity) {
                return new TodoDto(
                                entity.getId(),
                                entity.getDescription(),
                                entity.getStatus(),
                                entity.getCategories().stream()
                                                .filter(item -> item.getDeletedOn() == null)
                                                .map(CategoryDto::fromCategoryEntity)
                                                .collect(Collectors.toUnmodifiableSet()),
                                entity.getCreatedOn(),
                                entity.getUpdatedOn());
        }
}
