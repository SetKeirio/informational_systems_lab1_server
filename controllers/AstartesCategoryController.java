package informational_systems.lab1.controllers;

import informational_systems.lab1.items.AdminApproval;
import informational_systems.lab1.items.AstartesCategory;
import informational_systems.lab1.items.MeleeWeapon;
import informational_systems.lab1.items.Weapon;
import informational_systems.lab1.services.AdminApprovalService;
import informational_systems.lab1.services.AstartesCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/astartescategory")
public class AstartesCategoryController {
    @Autowired
    private AstartesCategoryService adminApprovalService;

    @GetMapping
    public List<AstartesCategory> getAllAdminApprovals() {
        return adminApprovalService.findAll();
    }

    public Optional<AstartesCategory> findById(Integer id) {
        return adminApprovalService.findById(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AstartesCategory> getWeaponById(@PathVariable Integer id) {
        return adminApprovalService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/names")
    public ResponseEntity<String[]> getAllNames() {
        List<AstartesCategory> names = adminApprovalService.findAll();
        return ResponseEntity.ok(names.stream()
                .map(AstartesCategory::getCategoryName)
                .collect(Collectors.toList()).toArray(new String[0]));
    }

    @GetMapping("/name/{categoryName}")
    public ResponseEntity<Integer> getIdByCategoryName(@PathVariable String categoryName) {
        Optional<AstartesCategory> category = adminApprovalService.findByName(categoryName);
        return category.map(cat -> ResponseEntity.ok(cat.getId()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public AstartesCategory createAdminApproval(@RequestBody AstartesCategory approval) {
        return adminApprovalService.save(approval);
    }
}