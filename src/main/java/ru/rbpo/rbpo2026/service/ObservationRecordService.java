package ru.rbpo.rbpo2026.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.rbpo.rbpo2026.entity.ObservationRecord;
import ru.rbpo.rbpo2026.repository.ObservationRecordRepository;
import ru.rbpo.rbpo2026.repository.ObservationSessionRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ObservationRecordService {

    private final ObservationRecordRepository recordRepository;
    private final ObservationSessionRepository sessionRepository;

    public List<ObservationRecord> getAll() {
        return recordRepository.findAll();
    }

    public ObservationRecord getById(Long id) {
        return recordRepository.findById(id);
    }

    public ObservationRecord create(ObservationRecord record) {
        checkSession(record.getSessionId());
        record.setId(null);
        record.setTime(LocalDateTime.now());
        return recordRepository.save(record);
    }

    public ObservationRecord update(Long id, ObservationRecord changes) {
        ObservationRecord record = recordRepository.findById(id);
        if (record == null) {
            return null;
        }
        if (changes.getSessionId() != null) {
            checkSession(changes.getSessionId());
            record.setSessionId(changes.getSessionId());
        }
        if (changes.getDescription() != null) {
            record.setDescription(changes.getDescription());
        }
        if (changes.getQuality() != null) {
            record.setQuality(changes.getQuality());
        }
        return recordRepository.save(record);
    }

    public boolean delete(Long id) {
        if (recordRepository.findById(id) == null) {
            return false;
        }
        recordRepository.deleteById(id);
        return true;
    }

    private void checkSession(Long id) {
        if (id == null || sessionRepository.findById(id) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Сеанс наблюдения не найден");
        }
    }
}
