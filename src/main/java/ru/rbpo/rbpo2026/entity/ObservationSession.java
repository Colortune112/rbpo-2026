package ru.rbpo.rbpo2026.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "observation_sessions", schema = "app")
@Getter
@Setter
@NoArgsConstructor
public class ObservationSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long celestialObjectId;
    private Long telescopeId;
    private Long observerId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
}
