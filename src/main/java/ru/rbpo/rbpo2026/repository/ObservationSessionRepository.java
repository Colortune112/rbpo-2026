package ru.rbpo.rbpo2026.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rbpo.rbpo2026.entity.ObservationSession;

import java.util.List;

public interface ObservationSessionRepository extends JpaRepository<ObservationSession, Long> {

    List<ObservationSession> findByTelescopeIdAndStatus(Long telescopeId, String status);

    boolean existsByTelescopeIdAndStatus(Long telescopeId, String status);

    boolean existsByObserverIdAndStatus(Long observerId, String status);

    boolean existsByCelestialObjectIdAndStatus(Long celestialObjectId, String status);
}
