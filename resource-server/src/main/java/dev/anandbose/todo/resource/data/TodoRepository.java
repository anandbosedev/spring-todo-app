package dev.anandbose.todo.resource.data;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, Long> {
    @Query("SELECT e FROM TodoEntity e WHERE e.id = :id AND e.owner = :owner AND e.deletedOn IS NULL")
    Optional<TodoEntity> findByIdAndOwner(Long id, String owner);

    @Query("SELECT e FROM TodoEntity e WHERE e.owner = :owner AND e.deletedOn IS NULL")
    Page<TodoEntity> findByOwner(String owner, Pageable pageable);

    @Query("SELECT e FROM TodoEntity e WHERE e.owner = :owner AND :category MEMBER OF e.categories AND e.deletedOn IS NULL")
    Page<TodoEntity> findByOwnerAndCategory(String owner, CategoryEntity category, Pageable pageable);
}
