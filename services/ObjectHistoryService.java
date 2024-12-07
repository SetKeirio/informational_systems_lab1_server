package informational_systems.lab1.services;

import informational_systems.lab1.items.ObjectHistory;
import informational_systems.lab1.repository.ObjectHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObjectHistoryService {
    @Autowired
    private ObjectHistoryRepository objectHistoryRepository;

    public List<ObjectHistory> findAll() {
        return objectHistoryRepository.findAll();
    }

    public ObjectHistory save(ObjectHistory history) {
        return objectHistoryRepository.save(history);
    }
}
