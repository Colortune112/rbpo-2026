package ru.rbpo.rbpo2026.entity;

import lombok.Data;

@Data
public class CelestialObject {

    private Long id;
    private String name;
    private String type;
    private Double minAperture;
}
