package informational_systems.lab1.controllers;

import informational_systems.lab1.items.AdminApproval;
import informational_systems.lab1.services.AdminApprovalService;
import informational_systems.lab1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/adminapprovals")
public class AdminApprovalController {
    @Autowired
    private AdminApprovalService adminApprovalService;

    @Autowired
    private UserService userService;  // Сервис для получения информации о пользователе

    @GetMapping
    public List<AdminApproval> getAllAdminApprovals() {
        return adminApprovalService.findAll();
    }

    @PostMapping
    public AdminApproval createAdminApproval(@RequestBody AdminApproval approval) {
        return adminApprovalService.save(approval);
    }

    @GetMapping("/request-admin-rigths/{id}")
    public ResponseEntity<String> requestAdminRights(@PathVariable Integer id) {
        try {
            // Пытаемся добавить запрос на админские права
            adminApprovalService.addAdminApprovalRequest(id);
            return ResponseEntity.ok("Запрос на получение прав администратора отправлен.");
        } catch (IllegalStateException e) {
            // Если возникла ошибка, возвращаем сообщение с ошибкой
            return ResponseEntity.status(400).body(e.getMessage());
        }
        catch (IllegalCallerException e) {
            // Если возникла ошибка, возвращаем сообщение с ошибкой
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @GetMapping("/admin-requests")
    public ResponseEntity<List<AdminApproval>> getAdminRequests() {
        List<AdminApproval> requests = adminApprovalService.getAllRequests(); // Замените на ваш метод
        return ResponseEntity.ok(requests);
    }

}
