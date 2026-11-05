package ru.rbpo.rbpo2026.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.CsrfException;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SecurityErrorHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    // 401 — не вошёл: нет логина, неверный пароль или учётная запись заблокирована
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException e) throws IOException {
        response.setHeader("WWW-Authenticate", "Basic realm=\"rbpo\"");
        write(response, 401, "Требуется вход: неверный логин или пароль, либо учётная запись заблокирована");
    }

    // 403 — вошёл, но не хватает прав (или нет CSRF-токена)
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException e) throws IOException {
        if (e instanceof CsrfException) {
            write(response, 403, "Нет или неверный CSRF-токен");
        } else {
            write(response, 403, "Недостаточно прав для этой операции");
        }
    }

    private void write(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"status\":" + status + ",\"message\":\"" + message + "\"}");
    }
}
