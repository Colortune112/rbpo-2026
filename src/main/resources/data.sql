INSERT INTO app.users (name, email)
VALUES ('Роман', 'roman@mail.ru'),
       ('Анна', 'anna@mail.ru');

INSERT INTO app.celestial_objects (name, type, min_aperture)
VALUES ('Луна', 'спутник', 30),
       ('Туманность Ориона', 'туманность', 50),
       ('Галактика Андромеды', 'галактика', 100);

INSERT INTO app.telescopes (name, aperture, available, maintenance)
VALUES ('Celestron 8', 203, TRUE, FALSE),
       ('Sky-Watcher 70', 70, TRUE, FALSE);

INSERT INTO app.observation_sessions (celestial_object_id, telescope_id, observer_id, start_time, end_time, status)
VALUES (2, 1, 1, '2026-11-01 22:00', '2026-11-02 01:00', 'PLANNED'),
       (1, 2, 2, '2026-10-15 20:00', '2026-10-15 21:00', 'COMPLETED');

INSERT INTO app.observation_records (session_id, record_time, description, quality)
VALUES (2, '2026-10-15 20:30', 'Хорошо видны кратеры', 4);
