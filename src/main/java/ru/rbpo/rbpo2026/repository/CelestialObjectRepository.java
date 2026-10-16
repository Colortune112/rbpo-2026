package ru.rbpo.rbpo2026.repository;

import org.springframework.stereotype.Repository;
import ru.rbpo.rbpo2026.entity.CelestialObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CelestialObjectRepository {

    private final Map<Long, CelestialObject> objects = new HashMap<>();
    private long nextId = 1;

    public List<CelestialObject> findAll() {
        return new ArrayList<>(objects.values());
    }

    public CelestialObject findById(Long id) {
        return objects.get(id);
    }

    public CelestialObject save(CelestialObject object) {
        if (object.getId() == null) {
            object.setId(nextId);
            nextId++;
        }
        objects.put(object.getId(), object);
        return object;
    }

    public void deleteById(Long id) {
        objects.remove(id);
    }
}
