package informational_systems.lab1.controllers;

import informational_systems.lab1.items.AdminApproval;
import informational_systems.lab1.items.UserSpaceMarines;
import informational_systems.lab1.services.AdminApprovalService;
import informational_systems.lab1.services.UserSpaceMarinesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usermarines")
public class UserSpaceMarinesController {
    @Autowired
    private UserSpaceMarinesService adminApprovalService;

    @GetMapping
    public List<UserSpaceMarines> getAllAdminApprovals() {
        return adminApprovalService.findAll();
    }

    @PostMapping
    public UserSpaceMarines createAdminApproval(@RequestBody UserSpaceMarines approval) {
        return adminApprovalService.save(approval);
    }
}