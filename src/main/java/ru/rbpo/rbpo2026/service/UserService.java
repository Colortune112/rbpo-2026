package ru.rbpo.rbpo2026.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.rbpo.rbpo2026.entity.User;
import ru.rbpo.rbpo2026.repository.ObservationSessionRepository;
import ru.rbpo.rbpo2026.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ObservationSessionRepository sessionRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User create(User user) {
        user.setId(null);
        return userRepository.save(user);
    }

    public User update(Long id, User changes) {
        User user = userRepository.findById(id).orElse(null);
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
        if (!userRepository.existsById(id)) {
            return false;
        }
        if (sessionRepository.existsByObserverIdAndStatus(id, "IN_PROGRESS")) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "У пользователя идёт сеанс наблюдения");
        }
        userRepository.deleteById(id);
        return true;
    }
}
