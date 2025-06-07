package dev.anandbose.todo.resource.controller.todo;

import java.util.List;

public record CreateTodoRequest(String description, List<Long> categories) {
    
}
