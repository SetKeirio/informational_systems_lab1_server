package informational_systems.lab1.controllers;

import informational_systems.lab1.items.AstartesCategory;
import informational_systems.lab1.items.MeleeWeapon;
import informational_systems.lab1.items.Weapon;
import informational_systems.lab1.services.MeleeWeaponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/meleeweapons")
public class MeleeWeaponController {
    @Autowired
    private MeleeWeaponService meleeWeaponService;

    @GetMapping
    public List<MeleeWeapon> getAllMeleeWeapons() {
        return meleeWeaponService.findAll();
    }

    @GetMapping("/names")
    public ResponseEntity<String[]> getAllNames() {
        List<MeleeWeapon> names = meleeWeaponService.findAll();
        return ResponseEntity.ok(names.stream()
                .map(MeleeWeapon::getMeleeWeaponName)
                .collect(Collectors.toList()).toArray(new String[0]));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeleeWeapon> getWeaponById(@PathVariable Integer id) {
        return meleeWeaponService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public MeleeWeapon createMeleeWeapon(@RequestBody MeleeWeapon weapon) {
        return meleeWeaponService.save(weapon);
    }
}