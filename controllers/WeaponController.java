package informational_systems.lab1.controllers;

import informational_systems.lab1.items.Weapon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import informational_systems.lab1.services.WeaponService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/weapons")
public class WeaponController {

    @Autowired
    private WeaponService weaponService;

    @GetMapping
    public List<Weapon> getAllWeapons() {
        return weaponService.findAll();
    }

    @GetMapping("/names")
    public ResponseEntity<String[]> getAllNames() {
        List<Weapon> names = weaponService.findAll();
        return ResponseEntity.ok(names.stream()
                .map(Weapon::getWeaponName)
                .collect(Collectors.toList()).toArray(new String[0]));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Weapon> getWeaponById(@PathVariable Integer id) {
        return weaponService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Weapon createWeapon(@RequestBody Weapon weapon) {
        return weaponService.save(weapon);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWeapon(@PathVariable Integer id) {
        weaponService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
