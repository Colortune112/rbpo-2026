package ru.rbpo.rbpo2026.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.rbpo.rbpo2026.entity.ObservationRecord;
import ru.rbpo.rbpo2026.entity.ObservationSession;
import ru.rbpo.rbpo2026.repository.CelestialObjectRepository;
import ru.rbpo.rbpo2026.repository.ObservationRecordRepository;
import ru.rbpo.rbpo2026.repository.ObservationSessionRepository;
import ru.rbpo.rbpo2026.repository.TelescopeRepository;
import ru.rbpo.rbpo2026.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ObservationSessionService {

    private final ObservationSessionRepository sessionRepository;
    private final ObservationRecordRepository recordRepository;
    private final CelestialObjectRepository celestialObjectRepository;
    private final TelescopeRepository telescopeRepository;
    private final UserRepository userRepository;

    public List<ObservationSession> getAll() {
        return sessionRepository.findAll();
    }

    public ObservationSession getById(Long id) {
        return sessionRepository.findById(id);
    }

    public ObservationSession create(ObservationSession session) {
        checkCelestialObject(session.getCelestialObjectId());
        checkTelescope(session.getTelescopeId());
        checkObserver(session.getObserverId());
        session.setId(null);
        if (session.getStatus() == null) {
            session.setStatus("PLANNED");
        }
        return sessionRepository.save(session);
    }

    public ObservationSession update(Long id, ObservationSession changes) {
        ObservationSession session = sessionRepository.findById(id);
        if (session == null) {
            return null;
        }
        if (changes.getCelestialObjectId() != null) {
            checkCelestialObject(changes.getCelestialObjectId());
            session.setCelestialObjectId(changes.getCelestialObjectId());
        }
        if (changes.getTelescopeId() != null) {
            checkTelescope(changes.getTelescopeId());
            session.setTelescopeId(changes.getTelescopeId());
        }
        if (changes.getObserverId() != null) {
            checkObserver(changes.getObserverId());
            session.setObserverId(changes.getObserverId());
        }
        if (changes.getStartTime() != null) {
            session.setStartTime(changes.getStartTime());
        }
        if (changes.getEndTime() != null) {
            session.setEndTime(changes.getEndTime());
        }
        if (changes.getStatus() != null) {
            session.setStatus(changes.getStatus());
        }
        return sessionRepository.save(session);
    }

    public boolean delete(Long id) {
        if (sessionRepository.findById(id) == null) {
            return false;
        }
        for (ObservationRecord record : recordRepository.findAll()) {
            if (id.equals(record.getSessionId())) {
                recordRepository.deleteById(record.getId());
            }
        }
        sessionRepository.deleteById(id);
        return true;
    }

    public void deleteByCelestialObjectId(Long celestialObjectId) {
        for (ObservationSession session : sessionRepository.findAll()) {
            if (celestialObjectId.equals(session.getCelestialObjectId())) {
                delete(session.getId());
            }
        }
    }

    public void deleteByTelescopeId(Long telescopeId) {
        for (ObservationSession session : sessionRepository.findAll()) {
            if (telescopeId.equals(session.getTelescopeId())) {
                delete(session.getId());
            }
        }
    }

    public void deleteByObserverId(Long observerId) {
        for (ObservationSession session : sessionRepository.findAll()) {
            if (observerId.equals(session.getObserverId())) {
                delete(session.getId());
            }
        }
    }

    private void checkCelestialObject(Long id) {
        if (id == null || celestialObjectRepository.findById(id) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Небесный объект не найден");
        }
    }

    private void checkTelescope(Long id) {
        if (id == null || telescopeRepository.findById(id) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Телескоп не найден");
        }
    }

    private void checkObserver(Long id) {
        if (id == null || userRepository.findById(id) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Наблюдатель не найден");
        }
    }
}
