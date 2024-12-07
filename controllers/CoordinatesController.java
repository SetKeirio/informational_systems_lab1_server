package informational_systems.lab1.controllers;

import informational_systems.lab1.items.Coordinates;
import informational_systems.lab1.items.SpaceMarine;
import informational_systems.lab1.services.CoordinatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coordinates")
public class CoordinatesController {
    @Autowired
    private CoordinatesService coordinatesService;

    @GetMapping
    public List<Coordinates> getAllCoordinates() {
        return coordinatesService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Coordinates> getSpaceMarineById(@PathVariable int id) {
        Coordinates coordinates = coordinatesService.findById(id);
        if (coordinates != null) {
            return ResponseEntity.ok(coordinates);
        }
        return ResponseEntity.notFound().build(); // Возвращаем 404, если космодесантник не найден
    }

    @PostMapping
    public Coordinates createCoordinates(@RequestBody Coordinates coordinates) {
        return coordinatesService.save(coordinates);
    }
}