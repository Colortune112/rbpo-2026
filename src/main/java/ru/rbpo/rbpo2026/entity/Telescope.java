package ru.rbpo.rbpo2026.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "telescopes", schema = "app")
@Getter
@Setter
@NoArgsConstructor
public class Telescope {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double aperture;
    private Boolean available;
    private Boolean maintenance;
}
