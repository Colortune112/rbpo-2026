package ru.rbpo.rbpo2026.repository;

import org.springframework.stereotype.Repository;
import ru.rbpo.rbpo2026.entity.Telescope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TelescopeRepository {

    private final Map<Long, Telescope> telescopes = new HashMap<>();
    private long nextId = 1;

    public List<Telescope> findAll() {
        return new ArrayList<>(telescopes.values());
    }

    public Telescope findById(Long id) {
        return telescopes.get(id);
    }

    public Telescope save(Telescope telescope) {
        if (telescope.getId() == null) {
            telescope.setId(nextId);
            nextId++;
        }
        telescopes.put(telescope.getId(), telescope);
        return telescope;
    }

    public void deleteById(Long id) {
        telescopes.remove(id);
    }
}
