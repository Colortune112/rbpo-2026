package ru.rbpo.rbpo2026.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rbpo.rbpo2026.entity.CelestialObject;
import ru.rbpo.rbpo2026.repository.CelestialObjectRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CelestialObjectService {

    private final CelestialObjectRepository celestialObjectRepository;
    private final ObservationSessionService observationSessionService;

    public List<CelestialObject> getAll() {
        return celestialObjectRepository.findAll();
    }

    public CelestialObject getById(Long id) {
        return celestialObjectRepository.findById(id);
    }

    public CelestialObject create(CelestialObject object) {
        object.setId(null);
        return celestialObjectRepository.save(object);
    }

    public CelestialObject update(Long id, CelestialObject changes) {
        CelestialObject object = celestialObjectRepository.findById(id);
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
        if (celestialObjectRepository.findById(id) == null) {
            return false;
        }
        observationSessionService.deleteByCelestialObjectId(id);
        celestialObjectRepository.deleteById(id);
        return true;
    }
}
