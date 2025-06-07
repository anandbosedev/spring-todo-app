package dev.anandbose.todo.resource.data;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    @Query("SELECT c FROM CategoryEntity c WHERE c.id = :id AND c.owner = :owner AND c.deletedOn IS NULL")
    Optional<CategoryEntity> findByIdAndOwner(Long id, String owner);

    @Query("SELECT c FROM CategoryEntity c WHERE c.owner = :owner AND c.deletedOn IS NULL")
    Stream<CategoryEntity> findByOwner(String owner);

    @Query("SELECT c FROM CategoryEntity c WHERE c.owner = :owner AND c.id IN :ids AND c.deletedOn IS NULL")
    List<CategoryEntity> findByMultipleIdAndOwner(List<Long> ids, String owner);
}
