package ru.rbpo.rbpo2026.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users", schema = "app")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;

    // пароль принимается в запросе, но никогда не отдаётся в ответе
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String role;
    private String status;
    private String name;
    private String email;
}
