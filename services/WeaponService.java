package informational_systems.lab1.services;

import informational_systems.lab1.items.Weapon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import informational_systems.lab1.repository.WeaponRepository;

import java.util.List;
import java.util.Optional;

@Service
public class WeaponService {

    @Autowired
    private WeaponRepository weaponRepository;

    public List<Weapon> findAll() {
        return weaponRepository.findAll();
    }

    public Optional<Weapon> findById(Integer id) {
        return weaponRepository.findById(id);
    }

    public Weapon save(Weapon weapon) {
        return weaponRepository.save(weapon);
    }

    public void deleteById(Integer id) {
        weaponRepository.deleteById(id);
    }

    public Optional<Integer> findIdByName(String name) {
        return weaponRepository.findByWeaponName(name)
                .map(Weapon::getId);
    }
}
