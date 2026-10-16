package ru.rbpo.rbpo2026.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rbpo.rbpo2026.entity.ObservationSession;
import ru.rbpo.rbpo2026.service.ObservationSessionService;

import java.util.List;

@RestController
@RequestMapping("/api/observation-sessions")
@RequiredArgsConstructor
public class ObservationSessionController {

    private final ObservationSessionService observationSessionService;

    @GetMapping
    public List<ObservationSession> getAll() {
        return observationSessionService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObservationSession> getById(@PathVariable Long id) {
        ObservationSession session = observationSessionService.getById(id);
        if (session == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(session);
    }

    @PostMapping
    public ResponseEntity<ObservationSession> create(@RequestBody ObservationSession session) {
        ObservationSession created = observationSessionService.create(session);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ObservationSession> update(@PathVariable Long id, @RequestBody ObservationSession changes) {
        ObservationSession session = observationSessionService.update(id, changes);
        if (session == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(session);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!observationSessionService.delete(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
