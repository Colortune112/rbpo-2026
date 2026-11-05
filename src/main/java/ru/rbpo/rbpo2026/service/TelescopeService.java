package ru.rbpo.rbpo2026.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.rbpo.rbpo2026.entity.ObservationSession;
import ru.rbpo.rbpo2026.entity.Telescope;
import ru.rbpo.rbpo2026.repository.ObservationSessionRepository;
import ru.rbpo.rbpo2026.repository.TelescopeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TelescopeService {

    private final TelescopeRepository telescopeRepository;
    private final ObservationSessionRepository sessionRepository;

    public List<Telescope> getAll() {
        return telescopeRepository.findAll();
    }

    public Telescope getById(Long id) {
        return telescopeRepository.findById(id).orElse(null);
    }

    public Telescope create(Telescope telescope) {
        telescope.setId(null);
        telescope.setAvailable(true);
        telescope.setMaintenance(false);
        return telescopeRepository.save(telescope);
    }

    public Telescope update(Long id, Telescope changes) {
        Telescope telescope = telescopeRepository.findById(id).orElse(null);
        if (telescope == null) {
            return null;
        }
        if (changes.getName() != null) {
            telescope.setName(changes.getName());
        }
        if (changes.getAperture() != null) {
            telescope.setAperture(changes.getAperture());
        }
        return telescopeRepository.save(telescope);
    }

    public boolean delete(Long id) {
        if (!telescopeRepository.existsById(id)) {
            return false;
        }
        if (sessionRepository.existsByTelescopeIdAndStatus(id, "IN_PROGRESS")) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "На телескопе идёт сеанс наблюдения");
        }
        telescopeRepository.deleteById(id);
        return true;
    }

    // Бизнес-операция: отправить телескоп на обслуживание
    @Transactional
    public Telescope sendToMaintenance(Long id) {
        Telescope telescope = telescopeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Телескоп не найден"));
        if (telescope.getMaintenance()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Телескоп уже на обслуживании");
        }
        if (sessionRepository.existsByTelescopeIdAndStatus(id, "IN_PROGRESS")) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "На телескопе идёт сеанс наблюдения");
        }
        for (ObservationSession session : sessionRepository.findByTelescopeIdAndStatus(id, "PLANNED")) {
            session.setStatus("CANCELLED");
            sessionRepository.save(session);
        }
        telescope.setMaintenance(true);
        telescope.setAvailable(false);
        return telescopeRepository.save(telescope);
    }

    // Бизнес-операция: вернуть телескоп с обслуживания
    @Transactional
    public Telescope returnFromMaintenance(Long id) {
        Telescope telescope = telescopeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Телескоп не найден"));
        if (!telescope.getMaintenance()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Телескоп не на обслуживании");
        }
        telescope.setMaintenance(false);
        telescope.setAvailable(true);
        return telescopeRepository.save(telescope);
    }
}
