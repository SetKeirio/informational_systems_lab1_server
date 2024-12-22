package informational_systems.lab1.services;

import informational_systems.lab1.items.ImportHistory;
import informational_systems.lab1.items.User;
import informational_systems.lab1.repository.ImportHistoryRepository;
import informational_systems.lab1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImportHistoryService {

    @Autowired
    private ImportHistoryRepository importHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    // Метод для добавления новой записи импорта
    public ImportHistory createImportHistory(String username, String status, Integer count, String description) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        ImportHistory importHistory = new ImportHistory();
        importHistory.setUsername(username);
        importHistory.setStatus(status);
        importHistory.setCount(count);
        importHistory.setDescription(description);

        return importHistoryRepository.save(importHistory);
    }

    public List<ImportHistory> findAll() {
        return importHistoryRepository.findAll();
    }
}
