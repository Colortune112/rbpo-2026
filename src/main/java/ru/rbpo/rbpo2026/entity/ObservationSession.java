package ru.rbpo.rbpo2026.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ObservationSession {

    private Long id;
    private Long celestialObjectId;
    private Long telescopeId;
    private Long observerId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
}
