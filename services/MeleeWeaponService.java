package informational_systems.lab1.services;

import informational_systems.lab1.items.MeleeWeapon;
import informational_systems.lab1.items.Weapon;
import informational_systems.lab1.repository.MeleeWeaponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MeleeWeaponService {
    @Autowired
    private MeleeWeaponRepository meleeWeaponRepository;

    public List<MeleeWeapon> findAll() {
        return meleeWeaponRepository.findAll();
    }

    public Optional<MeleeWeapon> findById(Integer id) {
        return meleeWeaponRepository.findById(id);
    }

    public MeleeWeapon save(MeleeWeapon weapon) {
        return meleeWeaponRepository.save(weapon);
    }

    public Optional<Integer> findIdByName(String name) {
        return meleeWeaponRepository.findByMeleeWeaponName(name)
                .map(MeleeWeapon::getId);
    }
}