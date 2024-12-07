package informational_systems.lab1.services;

import informational_systems.lab1.items.Coordinates;
import informational_systems.lab1.items.SpaceMarine;
import informational_systems.lab1.repository.CoordinatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CoordinatesService {
    @Autowired
    private CoordinatesRepository coordinatesRepository;

    public List<Coordinates> findAll() {
        return coordinatesRepository.findAll();
    }

    public Coordinates findById(int id) {
        Optional<Coordinates> optionalMarine = coordinatesRepository.findById(id);
        return optionalMarine.orElse(null); // Возвращаем null, если космодесантник не найден
    }

    public Coordinates save(Coordinates coordinates) {
        return coordinatesRepository.save(coordinates);
    }
}