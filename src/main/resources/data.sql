-- Пароли (хранятся как BCrypt-хеши): admin / Admin123!, roman / Roman123!, anna / Anna1234!, blocked / Blocked123!
INSERT INTO app.users (username, password, role, status, name, email)
VALUES ('admin', '$2a$10$5gz.2UWYXvrZc/pH94JeiOd9N4sn97fRtcf6pbxg8EcNAeNkZI3bK', 'ADMIN', 'ACTIVE', 'Администратор', 'admin@mail.ru'),
       ('roman', '$2a$10$Ze9zp5m.Eq9TKWe/Gkifpu1GIVMxCNgJdRC8LaS7SKllmrICKZvfy', 'OBSERVER', 'ACTIVE', 'Роман', 'roman@mail.ru'),
       ('anna', '$2a$10$BtaxdkQvEyjoi3.liyJkW.kTDfkjZCm0ye8aBpZXIr1lpqkHg6UaO', 'OBSERVER', 'ACTIVE', 'Анна', 'anna@mail.ru'),
       ('blocked', '$2a$10$2/8ejGsD/7fK0AyjjEaFWus97YLhrgmyK3UDU1BlTrNWscfZhnkSu', 'OBSERVER', 'BLOCKED', 'Заблокированный', 'blocked@mail.ru');

INSERT INTO app.celestial_objects (name, type, min_aperture)
VALUES ('Луна', 'спутник', 30),
       ('Туманность Ориона', 'туманность', 50),
       ('Галактика Андромеды', 'галактика', 100);

INSERT INTO app.telescopes (name, aperture, available, maintenance)
VALUES ('Celestron 8', 203, TRUE, FALSE),
       ('Sky-Watcher 70', 70, TRUE, FALSE);

INSERT INTO app.observation_sessions (celestial_object_id, telescope_id, observer_id, start_time, end_time, status)
VALUES (2, 1, 2, '2026-12-01 22:00', '2026-12-02 01:00', 'PLANNED'),
       (1, 2, 3, '2026-10-15 20:00', '2026-10-15 21:00', 'COMPLETED');

INSERT INTO app.observation_records (session_id, record_time, description, quality)
VALUES (2, '2026-10-15 20:30', 'Хорошо видны кратеры', 4);
