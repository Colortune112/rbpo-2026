package ru.rbpo.rbpo2026.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rbpo.rbpo2026.entity.CelestialObject;

public interface CelestialObjectRepository extends JpaRepository<CelestialObject, Long> {
}
