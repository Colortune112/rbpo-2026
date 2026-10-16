package ru.rbpo.rbpo2026.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rbpo.rbpo2026.entity.CelestialObject;
import ru.rbpo.rbpo2026.service.CelestialObjectService;

import java.util.List;

@RestController
@RequestMapping("/api/celestial-objects")
@RequiredArgsConstructor
public class CelestialObjectController {

    private final CelestialObjectService celestialObjectService;

    @GetMapping
    public List<CelestialObject> getAll() {
        return celestialObjectService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CelestialObject> getById(@PathVariable Long id) {
        CelestialObject object = celestialObjectService.getById(id);
        if (object == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(object);
    }

    @PostMapping
    public ResponseEntity<CelestialObject> create(@RequestBody CelestialObject object) {
        CelestialObject created = celestialObjectService.create(object);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CelestialObject> update(@PathVariable Long id, @RequestBody CelestialObject changes) {
        CelestialObject object = celestialObjectService.update(id, changes);
        if (object == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(object);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!celestialObjectService.delete(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
