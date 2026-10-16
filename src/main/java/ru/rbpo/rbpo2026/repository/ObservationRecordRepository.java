package ru.rbpo.rbpo2026.repository;

import org.springframework.stereotype.Repository;
import ru.rbpo.rbpo2026.entity.ObservationRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ObservationRecordRepository {

    private final Map<Long, ObservationRecord> records = new HashMap<>();
    private long nextId = 1;

    public List<ObservationRecord> findAll() {
        return new ArrayList<>(records.values());
    }

    public ObservationRecord findById(Long id) {
        return records.get(id);
    }

    public ObservationRecord save(ObservationRecord record) {
        if (record.getId() == null) {
            record.setId(nextId);
            nextId++;
        }
        records.put(record.getId(), record);
        return record;
    }

    public void deleteById(Long id) {
        records.remove(id);
    }
}
