package ru.rbpo.rbpo2026.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ObservationRecord {

    private Long id;
    private Long sessionId;
    private LocalDateTime time;
    private String description;
    private Integer quality;
}
