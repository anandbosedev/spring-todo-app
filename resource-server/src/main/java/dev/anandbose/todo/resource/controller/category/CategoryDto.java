package dev.anandbose.todo.resource.controller.category;

import dev.anandbose.todo.resource.data.CategoryEntity;

public record CategoryDto(Long id, String name) {
    public static CategoryDto fromCategoryEntity(CategoryEntity entity) {
        return new CategoryDto(entity.getId(), entity.getName());
    }
}
