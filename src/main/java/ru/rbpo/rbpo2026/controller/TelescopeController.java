package ru.rbpo.rbpo2026.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rbpo.rbpo2026.entity.Telescope;
import ru.rbpo.rbpo2026.service.TelescopeService;

import java.util.List;

@RestController
@RequestMapping("/api/telescopes")
@RequiredArgsConstructor
public class TelescopeController {

    private final TelescopeService telescopeService;

    @GetMapping
    public List<Telescope> getAll() {
        return telescopeService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Telescope> getById(@PathVariable Long id) {
        Telescope telescope = telescopeService.getById(id);
        if (telescope == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(telescope);
    }

    @PostMapping
    public ResponseEntity<Telescope> create(@RequestBody Telescope telescope) {
        Telescope created = telescopeService.create(telescope);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Telescope> update(@PathVariable Long id, @RequestBody Telescope changes) {
        Telescope telescope = telescopeService.update(id, changes);
        if (telescope == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(telescope);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!telescopeService.delete(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
