package informational_systems.lab1.repository;

import informational_systems.lab1.items.MeleeWeapon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MeleeWeaponRepository extends JpaRepository<MeleeWeapon, Integer> {
    Optional<MeleeWeapon> findByMeleeWeaponName(String name);
}