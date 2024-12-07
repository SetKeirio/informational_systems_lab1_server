package informational_systems.lab1.repository;

import informational_systems.lab1.items.ObjectHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectHistoryRepository extends JpaRepository<ObjectHistory, Integer> {
}