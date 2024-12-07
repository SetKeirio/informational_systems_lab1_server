package informational_systems.lab1.controllers;

import informational_systems.lab1.items.ObjectHistory;
import informational_systems.lab1.services.ObjectHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/objecthistory")
public class ObjectHistoryController {
    @Autowired
    private ObjectHistoryService objectHistoryService;

    @GetMapping
    public List<ObjectHistory> getAllObjectHistories() {
        return objectHistoryService.findAll();
    }

    @PostMapping
    public ObjectHistory createObjectHistory(@RequestBody ObjectHistory history) {
        return objectHistoryService.save(history);
    }
}