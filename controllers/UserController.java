package informational_systems.lab1.controllers;

import informational_systems.lab1.items.LoginRequest;
import informational_systems.lab1.items.LoginResponse;
import informational_systems.lab1.items.User;
import informational_systems.lab1.services.AdminApprovalService;
import informational_systems.lab1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AdminApprovalService adminApprovalService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.save(user);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") int id) {
        User user = userService.findById(id); // Ищем пользователя по ID
        if (user != null) {
            return ResponseEntity.ok(user.getUsername()); // Возвращаем пользователя
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден");
        }
    }

    @GetMapping("/{id}/approve")
    public ResponseEntity<String> approveUser(@PathVariable Integer id) {
        try {
            // Обновляем роль пользователя на ADMIN
            userService.updateUserRole(id, "ADMIN");
            User user = userService.findById(id);

            // Обновляем статус запроса на админские права на APPROVED
            adminApprovalService.approveRequest(user.getId());

            return ResponseEntity.ok("Пользователь одобрен и запрос на админские права подтвержден.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(400).body("Ошибка при подтверждении пользователя: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/reject")
    public ResponseEntity<String> rejectUser(@PathVariable Integer id) {
        try {
            // Обновляем роль пользователя на USER
            userService.updateUserRole(id, "USER");
            User user = userService.findById(id);

            // Обновляем статус запроса на админские права на DENIED
            adminApprovalService.denyRequest(user.getId());

            return ResponseEntity.ok("Пользователь отклонен и запрос на админские права отклонен.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(400).body("Ошибка при отклонении пользователя: " + e.getMessage());
        }
    }

}