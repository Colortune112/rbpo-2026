package ru.rbpo.rbpo2026.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rbpo.rbpo2026.entity.ObservationRecord;

public interface ObservationRecordRepository extends JpaRepository<ObservationRecord, Long> {

    long countBySessionId(Long sessionId);
}
