package informational_systems.lab1.controllers;

import informational_systems.lab1.items.LoginRequest;
import informational_systems.lab1.items.LoginResponse;
import informational_systems.lab1.items.RegisterRequest;
import informational_systems.lab1.items.User;
import informational_systems.lab1.repository.UserRepository;
import informational_systems.lab1.services.AuthentifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/login")
public class AuthentifyController {

    @Autowired
    private UserRepository userRepository; // Репозиторий для работы с таблицей users
    @Autowired
    private AuthentifyService authService;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername());
        if (user != null && hashPassword(loginRequest.getPassword()).equals(user.getPassword())) {
            String token = authService.generateToken(user.getUsername(), user.getRole(), user.getId());
            int userId = user.getId();
            return ResponseEntity.ok(new LoginResponse(token, userId));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверное имя пользователя или пароль.");
        }
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        // Проверка, существует ли пользователь с таким именем
        if (userRepository.findByUsername(registerRequest.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Пользователь с таким именем уже существует.");
        }

        // Создание нового пользователя
        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setPassword(hashPassword(registerRequest.getPassword()));
        newUser.setRole("USER"); // Или задайте роль по умолчанию
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(newUser); // Сохраняем пользователя

        return ResponseEntity.ok(new LoginResponse("Пользователь успешно зарегистрирован.", newUser.getId()));
    }

    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestBody String token) {
        boolean isValid = authService.isTokenValid(token);

        if (isValid) {
            return ResponseEntity.ok("Токен валиден");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Токен невалиден или истек");
        }
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}