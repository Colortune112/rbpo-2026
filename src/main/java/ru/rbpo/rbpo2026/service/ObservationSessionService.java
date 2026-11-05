package ru.rbpo.rbpo2026.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.rbpo.rbpo2026.entity.CelestialObject;
import ru.rbpo.rbpo2026.entity.ObservationSession;
import ru.rbpo.rbpo2026.entity.Telescope;
import ru.rbpo.rbpo2026.repository.CelestialObjectRepository;
import ru.rbpo.rbpo2026.repository.ObservationRecordRepository;
import ru.rbpo.rbpo2026.repository.ObservationSessionRepository;
import ru.rbpo.rbpo2026.repository.TelescopeRepository;
import ru.rbpo.rbpo2026.repository.UserRepository;

import java.time.LocalDateTime;
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
        return sessionRepository.findById(id).orElse(null);
    }

    public ObservationSession create(ObservationSession session) {
        if (session.getObserverId() == null || !userRepository.existsById(session.getObserverId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Наблюдатель не найден");
        }
        CelestialObject object = findCelestialObject(session.getCelestialObjectId());
        Telescope telescope = findTelescope(session.getTelescopeId());
        if (telescope.getAperture() < object.getMinAperture()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Апертура телескопа слишком мала для этого объекта");
        }
        checkTimes(session.getStartTime(), session.getEndTime());
        session.setId(null);
        session.setStatus("PLANNED");
        return sessionRepository.save(session);
    }

    public ObservationSession update(Long id, ObservationSession changes) {
        ObservationSession session = sessionRepository.findById(id).orElse(null);
        if (session == null) {
            return null;
        }
        if (changes.getObserverId() != null) {
            if (!userRepository.existsById(changes.getObserverId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Наблюдатель не найден");
            }
            session.setObserverId(changes.getObserverId());
        }
        if (changes.getCelestialObjectId() != null) {
            findCelestialObject(changes.getCelestialObjectId());
            session.setCelestialObjectId(changes.getCelestialObjectId());
        }
        if (changes.getTelescopeId() != null) {
            findTelescope(changes.getTelescopeId());
            session.setTelescopeId(changes.getTelescopeId());
        }
        // статус меняется только бизнес-операциями (start, complete, cancel)
        return sessionRepository.save(session);
    }

    public boolean delete(Long id) {
        ObservationSession session = sessionRepository.findById(id).orElse(null);
        if (session == null) {
            return false;
        }
        if (session.getStatus().equals("IN_PROGRESS")) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Сначала завершите сеанс");
        }
        sessionRepository.deleteById(id);
        return true;
    }

    // Бизнес-операция: начать сеанс
    @Transactional
    public ObservationSession start(Long id) {
        ObservationSession session = findSession(id);
        if (!session.getStatus().equals("PLANNED")) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Начать можно только запланированный сеанс");
        }
        Telescope telescope = findTelescope(session.getTelescopeId());
        if (telescope.getMaintenance()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Телескоп на обслуживании");
        }
        if (!telescope.getAvailable()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Телескоп занят другим сеансом");
        }
        telescope.setAvailable(false);
        telescopeRepository.save(telescope);
        session.setStatus("IN_PROGRESS");
        return sessionRepository.save(session);
    }

    // Бизнес-операция: завершить сеанс
    @Transactional
    public ObservationSession complete(Long id) {
        ObservationSession session = findSession(id);
        if (!session.getStatus().equals("IN_PROGRESS")) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Завершить можно только идущий сеанс");
        }
        if (recordRepository.countBySessionId(id) == 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Нельзя завершить сеанс без наблюдательных записей");
        }
        Telescope telescope = findTelescope(session.getTelescopeId());
        telescope.setAvailable(true);
        telescopeRepository.save(telescope);
        session.setStatus("COMPLETED");
        return sessionRepository.save(session);
    }

    // Бизнес-операция: отменить сеанс
    @Transactional
    public ObservationSession cancel(Long id) {
        ObservationSession session = findSession(id);
        if (!session.getStatus().equals("PLANNED")) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Отменить можно только запланированный сеанс");
        }
        session.setStatus("CANCELLED");
        return sessionRepository.save(session);
    }

    // Бизнес-операция: перенести сеанс на другое время
    @Transactional
    public ObservationSession reschedule(Long id, ObservationSession newTime) {
        ObservationSession session = findSession(id);
        if (!session.getStatus().equals("PLANNED")) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Перенести можно только запланированный сеанс");
        }
        if (newTime.getStartTime() == null || newTime.getEndTime() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Укажите startTime и endTime");
        }
        checkTimes(newTime.getStartTime(), newTime.getEndTime());
        if (newTime.getStartTime().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Нельзя перенести сеанс в прошлое");
        }
        for (ObservationSession other : sessionRepository.findByTelescopeIdAndStatus(session.getTelescopeId(), "PLANNED")) {
            if (!other.getId().equals(id) && other.getStartTime() != null && other.getEndTime() != null
                    && newTime.getStartTime().isBefore(other.getEndTime())
                    && newTime.getEndTime().isAfter(other.getStartTime())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Телескоп уже забронирован на это время (сеанс " + other.getId() + ")");
            }
        }
        session.setStartTime(newTime.getStartTime());
        session.setEndTime(newTime.getEndTime());
        return sessionRepository.save(session);
    }

    private ObservationSession findSession(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Сеанс наблюдения не найден"));
    }

    private Telescope findTelescope(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Телескоп не найден");
        }
        return telescopeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Телескоп не найден"));
    }

    private CelestialObject findCelestialObject(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Небесный объект не найден");
        }
        return celestialObjectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Небесный объект не найден"));
    }

    private void checkTimes(LocalDateTime start, LocalDateTime end) {
        if (start != null && end != null && !end.isAfter(start)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Конец сеанса должен быть позже начала");
        }
    }
}
