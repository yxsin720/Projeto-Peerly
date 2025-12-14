-- Peerly - populate.sql
-- Seed coherent, realistic test data. Safe to re-run.

USE peerly;

-- Users
INSERT INTO users (email, full_name, role) VALUES
('rita@peerly.app','Rita Fernandes','tutor')
ON DUPLICATE KEY UPDATE full_name=VALUES(full_name), role=VALUES(role);

INSERT INTO users (email, full_name, role) VALUES
('joao@peerly.app','João Silva','tutor')
ON DUPLICATE KEY UPDATE full_name=VALUES(full_name), role=VALUES(role);

INSERT INTO users (email, full_name, role) VALUES
('francisco@peerly.app','Francisco Lima','student')
ON DUPLICATE KEY UPDATE full_name=VALUES(full_name), role=VALUES(role);

-- Subjects
INSERT INTO subjects (slug, name) VALUES
('matematica','Matemática'),
('design','Design'),
('programacao','Programação')
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- Tutor subjects
INSERT INTO tutor_subjects (tutor_id, subject_id, level, price_per_hour_cents)
SELECT u.id, s.id, 'avançado', 2500
FROM users u, subjects s
WHERE u.email='rita@peerly.app' AND s.slug='design'
ON DUPLICATE KEY UPDATE level=VALUES(level), price_per_hour_cents=VALUES(price_per_hour_cents);

INSERT INTO tutor_subjects (tutor_id, subject_id, level, price_per_hour_cents)
SELECT u.id, s.id, 'intermédio', 2000
FROM users u, subjects s
WHERE u.email='joao@peerly.app' AND s.slug='matematica'
ON DUPLICATE KEY UPDATE level=VALUES(level), price_per_hour_cents=VALUES(price_per_hour_cents);

-- Availability
INSERT INTO tutor_availability (tutor_id, weekday, start_time, end_time, timezone)
SELECT id, 1, '14:00', '18:00', 'Europe/Lisbon'
FROM users WHERE email='rita@peerly.app'
LIMIT 1;

-- Session (only once per title per tutor)
INSERT INTO sessions (tutor_id, subject_id, title, description, starts_at, ends_at, status, max_participants, price_total_cents)
SELECT u.id, s.id, 'Design com Rita', 'Sessão introdutória de Design — UI/UX.',
       UTC_TIMESTAMP() + INTERVAL 2 DAY,
       UTC_TIMESTAMP() + INTERVAL 2 DAY + INTERVAL 1 HOUR,
       'scheduled', 1, 2500
FROM users u
JOIN subjects s ON s.slug='design'
WHERE u.email='rita@peerly.app'
  AND NOT EXISTS (
    SELECT 1 FROM sessions ss
    WHERE ss.tutor_id = u.id AND ss.title = 'Design com Rita'
  )
LIMIT 1;

-- Participant
INSERT INTO session_participants (session_id, user_id)
SELECT
  (SELECT id FROM sessions WHERE tutor_id=(SELECT id FROM users WHERE email='rita@peerly.app')
   ORDER BY created_at DESC LIMIT 1),
  (SELECT id FROM users WHERE email='francisco@peerly.app')
ON DUPLICATE KEY UPDATE joined_at = CURRENT_TIMESTAMP;

-- Sample review (if session exists and finished)
-- Note: You can finish the session first, then run this.
-- UPDATE sessions SET status='finished' WHERE title='Design com Rita';
-- INSERT INTO reviews (session_id, reviewer_id, tutor_id, rating, comment)
-- SELECT s.id, (SELECT id FROM users WHERE email='francisco@peerly.app'), s.tutor_id, 5, 'Excelente sessão!'
-- FROM sessions s WHERE s.title='Design com Rita'
-- ON DUPLICATE KEY UPDATE rating=VALUES(rating), comment=VALUES(comment);
