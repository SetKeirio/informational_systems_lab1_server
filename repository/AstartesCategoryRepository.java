package informational_systems.lab1.repository;

import informational_systems.lab1.items.AstartesCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AstartesCategoryRepository extends JpaRepository<AstartesCategory, Integer> {
    Optional<AstartesCategory> findByCategoryName(String name);
}