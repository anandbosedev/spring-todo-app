package dev.anandbose.todo.resource.controller.todo;

import dev.anandbose.todo.resource.data.TodoStatus;

public record UpdateTodoRequest(String description, TodoStatus status) {
    
}
