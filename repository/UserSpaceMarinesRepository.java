package informational_systems.lab1.repository;

import informational_systems.lab1.items.UserSpaceMarines;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSpaceMarinesRepository extends JpaRepository<UserSpaceMarines, Integer> {

    boolean existsByUserIdAndSpaceMarineId(int userId, int spaceMarineId);
}