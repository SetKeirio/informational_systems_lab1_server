package informational_systems.lab1.services;

import informational_systems.lab1.items.AdminApproval;
import informational_systems.lab1.items.AstartesCategory;
import informational_systems.lab1.items.Weapon;
import informational_systems.lab1.repository.AstartesCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AstartesCategoryService {
    @Autowired
    private AstartesCategoryRepository astartesCategoryRepository;

    public List<AstartesCategory> findAll() {
        return astartesCategoryRepository.findAll();
    }

    public Optional<AstartesCategory> findById(Integer id) {
        return astartesCategoryRepository.findById(id);
    }

    public Optional<AstartesCategory> findByName(String name) {
        return astartesCategoryRepository.findByCategoryName(name); // Предполагается, что у вас есть этот метод в репозитории
    }

    public AstartesCategory save(AstartesCategory approval) {
        return astartesCategoryRepository.save(approval);
    }

    public Optional<Integer> findIdByName(String name) {
        return astartesCategoryRepository.findByCategoryName(name)
                .map(AstartesCategory::getId);
    }
}