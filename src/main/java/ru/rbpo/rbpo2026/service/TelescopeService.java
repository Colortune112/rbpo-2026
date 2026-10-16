package ru.rbpo.rbpo2026.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rbpo.rbpo2026.entity.Telescope;
import ru.rbpo.rbpo2026.repository.TelescopeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TelescopeService {

    private final TelescopeRepository telescopeRepository;
    private final ObservationSessionService observationSessionService;

    public List<Telescope> getAll() {
        return telescopeRepository.findAll();
    }

    public Telescope getById(Long id) {
        return telescopeRepository.findById(id);
    }

    public Telescope create(Telescope telescope) {
        telescope.setId(null);
        return telescopeRepository.save(telescope);
    }

    public Telescope update(Long id, Telescope changes) {
        Telescope telescope = telescopeRepository.findById(id);
        if (telescope == null) {
            return null;
        }
        if (changes.getName() != null) {
            telescope.setName(changes.getName());
        }
        if (changes.getAperture() != null) {
            telescope.setAperture(changes.getAperture());
        }
        if (changes.getAvailable() != null) {
            telescope.setAvailable(changes.getAvailable());
        }
        return telescopeRepository.save(telescope);
    }

    public boolean delete(Long id) {
        if (telescopeRepository.findById(id) == null) {
            return false;
        }
        observationSessionService.deleteByTelescopeId(id);
        telescopeRepository.deleteById(id);
        return true;
    }
}
