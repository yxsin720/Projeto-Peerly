SELECT u.id, u.full_name, tr.avg_rating, tr.reviews_count
FROM users u
JOIN tutor_rating tr ON tr.tutor_id = u.id
WHERE u.role IN ('tutor','both')
ORDER BY tr.avg_rating DESC, tr.reviews_count DESC;

SELECT s.*
FROM sessions s
WHERE s.tutor_id = (SELECT id FROM users WHERE email = 'rita@peerly.app')
  AND s.starts_at >= UTC_TIMESTAMP()
ORDER BY s.starts_at;

SELECT s.id, s.title, s.starts_at, s.ends_at, s.status, t.full_name AS tutor_name
FROM session_participants sp
JOIN sessions s ON s.id = sp.session_id
JOIN users t ON t.id = s.tutor_id
WHERE sp.user_id = (SELECT id FROM users WHERE email='francisco@peerly.app')
ORDER BY s.starts_at DESC;

SELECT u.full_name, sub.name AS subject, ts.level, ts.price_per_hour_cents
FROM tutor_subjects ts
JOIN users u ON u.id = ts.tutor_id
JOIN subjects sub ON sub.id = ts.subject_id
WHERE sub.slug = 'design'
ORDER BY ts.price_per_hour_cents ASC;

SELECT u.full_name, tr.reviews_count, tr.avg_rating
FROM tutor_rating tr
JOIN users u ON u.id = tr.tutor_id
WHERE u.role IN ('tutor','both')
ORDER BY tr.reviews_count DESC
LIMIT 5;

SELECT m.id, m.sender_id, m.type, m.content, m.created_at
FROM messages m
WHERE m.session_id = (
  SELECT s.id FROM sessions s
  WHERE s.tutor_id = (SELECT id FROM users WHERE email='rita@peerly.app')
  ORDER BY s.created_at DESC LIMIT 1
)
ORDER BY m.created_at DESC
LIMIT 50;

SELECT n.*
FROM notifications n
WHERE n.user_id = (SELECT id FROM users WHERE email='francisco@peerly.app')
  AND n.is_read = 0
ORDER BY n.created_at DESC;

SELECT ta.tutor_id, u.full_name, ta.weekday, ta.start_time, ta.end_time, ta.timezone
FROM tutor_availability ta
JOIN users u ON u.id = ta.tutor_id
ORDER BY u.full_name, ta.weekday, ta.start_time;
