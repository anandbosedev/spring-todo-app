package dev.anandbose.todo.web.data;

import java.util.List;

public record PagedModel<T>(PagingMetadata page, List<T> content) {
    
}
