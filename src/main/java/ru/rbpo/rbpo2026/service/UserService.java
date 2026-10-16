package ru.rbpo.rbpo2026.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rbpo.rbpo2026.entity.User;
import ru.rbpo.rbpo2026.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ObservationSessionService observationSessionService;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.findById(id);
    }

    public User create(User user) {
        user.setId(null);
        return userRepository.save(user);
    }

    public User update(Long id, User changes) {
        User user = userRepository.findById(id);
        if (user == null) {
            return null;
        }
        if (changes.getName() != null) {
            user.setName(changes.getName());
        }
        if (changes.getEmail() != null) {
            user.setEmail(changes.getEmail());
        }
        return userRepository.save(user);
    }

    public boolean delete(Long id) {
        if (userRepository.findById(id) == null) {
            return false;
        }
        observationSessionService.deleteByObserverId(id);
        userRepository.deleteById(id);
        return true;
    }
}
