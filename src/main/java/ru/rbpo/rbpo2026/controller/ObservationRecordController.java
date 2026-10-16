package ru.rbpo.rbpo2026.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rbpo.rbpo2026.entity.ObservationRecord;
import ru.rbpo.rbpo2026.service.ObservationRecordService;

import java.util.List;

@RestController
@RequestMapping("/api/observation-records")
@RequiredArgsConstructor
public class ObservationRecordController {

    private final ObservationRecordService observationRecordService;

    @GetMapping
    public List<ObservationRecord> getAll() {
        return observationRecordService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObservationRecord> getById(@PathVariable Long id) {
        ObservationRecord record = observationRecordService.getById(id);
        if (record == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(record);
    }

    @PostMapping
    public ResponseEntity<ObservationRecord> create(@RequestBody ObservationRecord record) {
        ObservationRecord created = observationRecordService.create(record);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ObservationRecord> update(@PathVariable Long id, @RequestBody ObservationRecord changes) {
        ObservationRecord record = observationRecordService.update(id, changes);
        if (record == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(record);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!observationRecordService.delete(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
