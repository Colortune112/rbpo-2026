package ru.rbpo.rbpo2026.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.rbpo.rbpo2026.entity.ObservationRecord;
import ru.rbpo.rbpo2026.entity.ObservationSession;
import ru.rbpo.rbpo2026.entity.User;
import ru.rbpo.rbpo2026.repository.ObservationRecordRepository;
import ru.rbpo.rbpo2026.repository.ObservationSessionRepository;
import ru.rbpo.rbpo2026.repository.UserRepository;

// Проверки «по контексту»: владелец ли текущий пользователь этого сеанса или записи.
// Используются в @PreAuthorize как @accessChecker.isSessionOwner(#id)
@Component("accessChecker")
@RequiredArgsConstructor
public class AccessChecker {

    private final UserRepository userRepository;
    private final ObservationSessionRepository sessionRepository;
    private final ObservationRecordRepository recordRepository;

    public Long currentUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElse(null);
        return user == null ? null : user.getId();
    }

    public boolean isSessionOwner(Long sessionId) {
        if (sessionId == null) {
            return false;
        }
        ObservationSession session = sessionRepository.findById(sessionId).orElse(null);
        return session != null && session.getObserverId().equals(currentUserId());
    }

    public boolean isRecordOwner(Long recordId) {
        if (recordId == null) {
            return false;
        }
        ObservationRecord record = recordRepository.findById(recordId).orElse(null);
        return record != null && isSessionOwner(record.getSessionId());
    }
}
