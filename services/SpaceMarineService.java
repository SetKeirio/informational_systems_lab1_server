package informational_systems.lab1.services;

import informational_systems.lab1.items.SpaceMarine;
import informational_systems.lab1.items.SpaceMarineResponse;
import informational_systems.lab1.repository.SpaceMarineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SpaceMarineService {
    @Autowired
    private SpaceMarineRepository spaceMarineRepository;

    public List<SpaceMarine> findByCategoryId(Integer categoryId) {
        return spaceMarineRepository.findByCategoryId(categoryId);
    }

    public List<SpaceMarine> findAll() {
        return spaceMarineRepository.findAll();
    }

    public SpaceMarine save(SpaceMarine marine) {
        return spaceMarineRepository.save(marine);
    }

    public SpaceMarine findById(int id) {
        Optional<SpaceMarine> optionalMarine = spaceMarineRepository.findById(id);
        return optionalMarine.orElse(null); // Возвращаем null, если космодесантник не найден
    }

    public int disbandCategory(int categoryId) {
        List<SpaceMarine> marines = spaceMarineRepository.findByCategoryId(categoryId);

        for (SpaceMarine marine : marines) {
            marine.setCategoryId(null); // Устанавливаем categoryId в null
            spaceMarineRepository.save(marine); // Сохраняем изменения
        }

        return marines.size(); // Возвращаем количество обновленных космодесантников
    }

    public void delete(int id) {
        spaceMarineRepository.deleteById(id);
    }

    public List<SpaceMarineResponse> getAllSpaceMarines() {
        return spaceMarineRepository.findAllSpaceMarinesWithDetails();
    }

    public List<SpaceMarineResponse> getSpaceMarinesWithHealthLessThan(int maxHealth) {
        return spaceMarineRepository.findSpaceMarinesWithHealthLessThan(maxHealth);
    }

    public SpaceMarineResponse getSpaceMarineById(int id) {
        return spaceMarineRepository.findSpaceMarineById(id);
    }

    @Transactional
    public int removeAstartesCategoryFromSpaceMarines(Integer categoryId) {
        return spaceMarineRepository.setCategoryToNull(categoryId);
    }



}
