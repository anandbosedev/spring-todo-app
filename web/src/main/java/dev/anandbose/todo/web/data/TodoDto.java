package dev.anandbose.todo.web.data;

import java.util.Date;
import java.util.Set;

public record TodoDto(Long id, String description, TodoStatus status, Set<CategoryDto> categories, Date createdOn,
        Date updatedOn) {

}
