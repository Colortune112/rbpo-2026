package ru.rbpo.rbpo2026.entity;

import lombok.Data;

@Data
public class Telescope {

    private Long id;
    private String name;
    private Double aperture;
    private Boolean available;
}
