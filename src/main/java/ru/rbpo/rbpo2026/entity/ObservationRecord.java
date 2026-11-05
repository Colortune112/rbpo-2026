package ru.rbpo.rbpo2026.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "observation_records", schema = "app")
@Getter
@Setter
@NoArgsConstructor
public class ObservationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long sessionId;

    @Column(name = "record_time")
    private LocalDateTime time;

    private String description;
    private Integer quality;
}
