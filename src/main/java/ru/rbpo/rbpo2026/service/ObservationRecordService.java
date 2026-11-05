package ru.rbpo.rbpo2026.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.rbpo.rbpo2026.entity.ObservationRecord;
import ru.rbpo.rbpo2026.entity.ObservationSession;
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
        return recordRepository.findById(id).orElse(null);
    }

    // Запись можно добавить только в идущий сеанс; время ставит сервер
    public ObservationRecord create(ObservationRecord record) {
        if (record.getSessionId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Сеанс наблюдения не найден");
        }
        ObservationSession session = sessionRepository.findById(record.getSessionId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Сеанс наблюдения не найден"));
        if (!session.getStatus().equals("IN_PROGRESS")) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Записи можно добавлять только в идущий сеанс");
        }
        checkQuality(record.getQuality());
        record.setId(null);
        record.setTime(LocalDateTime.now());
        return recordRepository.save(record);
    }

    public ObservationRecord update(Long id, ObservationRecord changes) {
        ObservationRecord record = recordRepository.findById(id).orElse(null);
        if (record == null) {
            return null;
        }
        if (changes.getDescription() != null) {
            record.setDescription(changes.getDescription());
        }
        if (changes.getQuality() != null) {
            checkQuality(changes.getQuality());
            record.setQuality(changes.getQuality());
        }
        return recordRepository.save(record);
    }

    public boolean delete(Long id) {
        if (!recordRepository.existsById(id)) {
            return false;
        }
        recordRepository.deleteById(id);
        return true;
    }

    private void checkQuality(Integer quality) {
        if (quality == null || quality < 1 || quality > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Оценка качества должна быть от 1 до 5");
        }
    }
}
