package ru.rbpo.rbpo2026.repository;

import org.springframework.stereotype.Repository;
import ru.rbpo.rbpo2026.entity.ObservationSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ObservationSessionRepository {

    private final Map<Long, ObservationSession> sessions = new HashMap<>();
    private long nextId = 1;

    public List<ObservationSession> findAll() {
        return new ArrayList<>(sessions.values());
    }

    public ObservationSession findById(Long id) {
        return sessions.get(id);
    }

    public ObservationSession save(ObservationSession session) {
        if (session.getId() == null) {
            session.setId(nextId);
            nextId++;
        }
        sessions.put(session.getId(), session);
        return session;
    }

    public void deleteById(Long id) {
        sessions.remove(id);
    }
}
