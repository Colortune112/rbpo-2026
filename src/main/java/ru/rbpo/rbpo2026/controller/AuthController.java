package ru.rbpo.rbpo2026.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import ru.rbpo.rbpo2026.entity.User;
import ru.rbpo.rbpo2026.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // Получить CSRF-токен: он же придёт в cookie XSRF-TOKEN
    @GetMapping("/csrf")
    public Map<String, String> csrf(CsrfToken token) {
        return Map.of("headerName", token.getHeaderName(), "token", token.getToken());
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(user));
    }

    // Кто я: данные текущего пользователя
    @GetMapping("/me")
    public User me(Authentication authentication) {
        return userService.getByUsername(authentication.getName());
    }
}
