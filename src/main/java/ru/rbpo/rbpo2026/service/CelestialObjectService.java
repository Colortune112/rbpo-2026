package ru.rbpo.rbpo2026.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.rbpo.rbpo2026.entity.CelestialObject;
import ru.rbpo.rbpo2026.repository.ObservationSessionRepository;
import ru.rbpo.rbpo2026.repository.CelestialObjectRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CelestialObjectService {

    private final CelestialObjectRepository celestialObjectRepository;
    private final ObservationSessionRepository sessionRepository;

    public List<CelestialObject> getAll() {
        return celestialObjectRepository.findAll();
    }

    public CelestialObject getById(Long id) {
        return celestialObjectRepository.findById(id).orElse(null);
    }

    public CelestialObject create(CelestialObject object) {
        object.setId(null);
        return celestialObjectRepository.save(object);
    }

    public CelestialObject update(Long id, CelestialObject changes) {
        CelestialObject object = celestialObjectRepository.findById(id).orElse(null);
        if (object == null) {
            return null;
        }
        if (changes.getName() != null) {
            object.setName(changes.getName());
        }
        if (changes.getType() != null) {
            object.setType(changes.getType());
        }
        if (changes.getMinAperture() != null) {
            object.setMinAperture(changes.getMinAperture());
        }
        return celestialObjectRepository.save(object);
    }

    public boolean delete(Long id) {
        if (!celestialObjectRepository.existsById(id)) {
            return false;
        }
        if (sessionRepository.existsByCelestialObjectIdAndStatus(id, "IN_PROGRESS")) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Этот объект сейчас наблюдают");
        }
        celestialObjectRepository.deleteById(id);
        return true;
    }
}
