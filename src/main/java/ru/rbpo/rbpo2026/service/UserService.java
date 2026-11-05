package ru.rbpo.rbpo2026.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    // Регистрация: роль всегда OBSERVER, повысить себе права нельзя
    public User register(User user) {
        user.setRole("OBSERVER");
        user.setStatus("ACTIVE");
        return saveNewUser(user);
    }

    // Создание пользователя администратором: роль можно выбрать
    public User create(User user) {
        if (user.getRole() == null) {
            user.setRole("OBSERVER");
        }
        checkRole(user.getRole());
        user.setStatus("ACTIVE");
        return saveNewUser(user);
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
        if (changes.getRole() != null) {
            checkRole(changes.getRole());
            user.setRole(changes.getRole());
        }
        if (changes.getStatus() != null) {
            if (!changes.getStatus().equals("ACTIVE") && !changes.getStatus().equals("BLOCKED")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Статус может быть ACTIVE или BLOCKED");
            }
            user.setStatus(changes.getStatus());
        }
        if (changes.getPassword() != null) {
            checkPassword(changes.getPassword());
            user.setPassword(passwordEncoder.encode(changes.getPassword()));
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

    private User saveNewUser(User user) {
        if (isBlank(user.getUsername()) || isBlank(user.getPassword()) || isBlank(user.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Укажите username, password и name");
        }
        checkPassword(user.getPassword());
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Пользователь с таким логином уже есть");
        }
        user.setId(null);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Надёжный пароль: не короче 8 символов, есть буква и цифра
    private void checkPassword(String password) {
        boolean hasLetter = false;
        boolean hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            }
            if (Character.isDigit(c)) {
                hasDigit = true;
            }
        }
        if (password.length() < 8 || !hasLetter || !hasDigit) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Пароль должен быть не короче 8 символов и содержать буквы и цифры");
        }
    }

    private void checkRole(String role) {
        if (!role.equals("OBSERVER") && !role.equals("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Роль может быть OBSERVER или ADMIN");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
