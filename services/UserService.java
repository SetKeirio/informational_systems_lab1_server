package informational_systems.lab1.services;

import informational_systems.lab1.items.SpaceMarine;
import informational_systems.lab1.items.User;
import informational_systems.lab1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User findById(int id) {
        User user = userRepository.findById(id);
        return user; // Возвращаем null, если космодесантник не найден
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username); // Предполагается, что такой метод есть в репозитории
    }

    public void updateUserRole(Integer id, String role) {
        User user = userRepository.findById(id).orElse(null);
        user.setRole(role);
        userRepository.save(user);
    }

    public boolean isUserAdmin(Integer userId) {
        // Проверяем пользователя по ID
        User user = userRepository.findById(userId).orElse(null);
        return user != null && user.getRole().equals("ADMIN");  // Предположим, что у пользователя есть поле role
    }
}
