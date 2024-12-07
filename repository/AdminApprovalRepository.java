package informational_systems.lab1.repository;

import informational_systems.lab1.items.AdminApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminApprovalRepository extends JpaRepository<AdminApproval, Integer> {
    boolean existsByUserId(Integer userId);
    Optional<AdminApproval> findByUserId(Integer userId);
}