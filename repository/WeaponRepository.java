package informational_systems.lab1.repository;

import informational_systems.lab1.items.Weapon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeaponRepository extends JpaRepository<Weapon, Integer> {
    Optional<Weapon> findByWeaponName(String name);
}