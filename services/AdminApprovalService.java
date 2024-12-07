package informational_systems.lab1.services;

import informational_systems.lab1.items.AdminApproval;
import informational_systems.lab1.items.User;
import informational_systems.lab1.repository.AdminApprovalRepository;
import informational_systems.lab1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AdminApprovalService {
    @Autowired
    private AdminApprovalRepository adminApprovalRepository;

    @Autowired
    private UserRepository userRepository;
    public List<AdminApproval> findAll() {
        return adminApprovalRepository.findAll();
    }

    public AdminApproval save(AdminApproval approval) {
        return adminApprovalRepository.save(approval);
    }



    public void addAdminApprovalRequest(Integer id) {
        // Найдите пользователя по имени
        User user = userRepository.findById(id).orElse(null);

        if (user != null) {
            // Проверяем, является ли пользователь уже администратором
            if (user.getRole().equals("ADMIN")) {
                throw new IllegalCallerException("Пользователь уже является администратором.");
            }

            // Проверяем, существует ли уже запрос на получение прав администратора для этого пользователя
            boolean requestExists = adminApprovalRepository.existsByUserId(id);
            if (requestExists) {
                throw new IllegalStateException("Вы уже отправляли запрос на получение прав администратора.");
            }

            // Если пользователь не администратор и запрос не был отправлен, создаем новый запрос
            AdminApproval approval = new AdminApproval();
            approval.setUserId(user.getId());
            approval.setStatus("PENDING");
            approval.setCreatedAt(LocalDateTime.now());
            approval.setUpdatedAt(LocalDateTime.now());

            adminApprovalRepository.save(approval);
        }
    }

    public void approveRequest(Integer requestId) {
        Optional<AdminApproval> approval = adminApprovalRepository.findByUserId(requestId);
        if (approval.isPresent()) {
            AdminApproval adminApproval = approval.get();
            adminApproval.setStatus("APPROVED");
            adminApproval.setUpdatedAt(LocalDateTime.now());
            adminApprovalRepository.save(adminApproval);
        } else {
            throw new IllegalStateException("Запрос с таким ID не найден.");
        }
    }

    public void denyRequest(Integer requestId) {
        Optional<AdminApproval> approval = adminApprovalRepository.findByUserId(requestId);
        if (approval.isPresent()) {
            AdminApproval adminApproval = approval.get();
            adminApproval.setStatus("DENIED");
            adminApproval.setUpdatedAt(LocalDateTime.now());
            adminApprovalRepository.save(adminApproval);
        } else {
            throw new IllegalStateException("Запрос с таким ID не найден.");
        }
    }

    public List<AdminApproval> getAllRequests() {
        return adminApprovalRepository.findAll(); // Предполагается, что метод findAll() возвращает все записи
    }
}
