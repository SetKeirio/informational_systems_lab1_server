package informational_systems.lab1.services;

import informational_systems.lab1.items.User;
import informational_systems.lab1.items.UserSpaceMarines;
import informational_systems.lab1.repository.UserRepository;
import informational_systems.lab1.repository.UserSpaceMarinesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSpaceMarinesService {
    @Autowired
    private UserSpaceMarinesRepository usersRepository;

    public List<UserSpaceMarines> findAll() {
        return usersRepository.findAll();
    }

    public UserSpaceMarines save(UserSpaceMarines user) {
        return usersRepository.save(user);
    }

    public boolean doesUserOwnMarine(int userId, int spaceMarineId) {
        return usersRepository.existsByUserIdAndSpaceMarineId(userId, spaceMarineId);
    }

}
