package ru.rbpo.rbpo2026.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "celestial_objects", schema = "app")
@Getter
@Setter
@NoArgsConstructor
public class CelestialObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    private Double minAperture;
}
