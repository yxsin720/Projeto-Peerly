SELECT u.id,u.email,u.full_name,u.role,u.area,u.language,u.created_at
FROM users u
ORDER BY u.created_at DESC;

SELECT u.id,u.full_name,u.email,u.role,u.area,u.avatar_url
FROM users u
WHERE u.email='larissa@peerly.study';

SELECT u.id,u.full_name,u.email,u.role
FROM users u
WHERE u.role IN ('tutor','both')
ORDER BY u.full_name;

SELECT u.id,u.full_name,COUNT(r.id) AS reviews,ROUND(AVG(r.rating),2) AS rating
FROM users u
LEFT JOIN reviews r ON r.tutor_id=u.id
WHERE u.role IN ('tutor','both')
GROUP BY u.id
ORDER BY rating DESC, reviews DESC, u.full_name;

SELECT u.id,u.full_name,COUNT(ts.subject_id) AS subjects
FROM users u
LEFT JOIN tutor_subjects ts ON ts.tutor_id=u.id
WHERE u.role IN ('tutor','both')
GROUP BY u.id
ORDER BY subjects DESC, u.full_name;

SELECT s.id,s.slug,s.name
FROM subjects s
ORDER BY s.name;

SELECT u.id,u.full_name,s.slug,s.name,ts.level,ts.price_per_hour_cents
FROM tutor_subjects ts
JOIN users u ON u.id=ts.tutor_id
JOIN subjects s ON s.id=ts.subject_id
ORDER BY u.full_name, s.name;

SELECT u.id,u.full_name,u.email,ts.level,ts.price_per_hour_cents
FROM tutor_subjects ts
JOIN users u ON u.id=ts.tutor_id
JOIN subjects s ON s.id=ts.subject_id
WHERE s.slug='matematica'
ORDER BY ts.price_per_hour_cents ASC, u.full_name;

SELECT u.id,u.full_name,COUNT(f.user_id) AS favorites_count
FROM users u
LEFT JOIN favorites f ON f.tutor_id=u.id
WHERE u.role IN ('tutor','both')
GROUP BY u.id
ORDER BY favorites_count DESC, u.full_name;

SELECT u.id,u.full_name
FROM users u
WHERE u.role IN ('tutor','both')
AND EXISTS (
  SELECT 1 FROM favorites f
  JOIN users st ON st.id=f.user_id
  WHERE f.tutor_id=u.id AND st.email='larissa@peerly.study'
)
ORDER BY u.full_name;

SELECT u.id,u.full_name,u.email
FROM users u
WHERE u.role IN ('tutor','both')
AND u.full_name LIKE '%Pedro%'
ORDER BY u.full_name;

SELECT u.id,u.full_name,u.role,u.area
FROM users u
WHERE u.area='Matemática'
ORDER BY u.full_name;

SELECT ta.tutor_id,u.full_name,ta.weekday,ta.start_time,ta.end_time,ta.timezone
FROM tutor_availability ta
JOIN users u ON u.id=ta.tutor_id
WHERE u.email='pedro@peerly.study'
ORDER BY ta.weekday, ta.start_time;

SELECT tto.tutor_id,u.full_name,tto.start_at,tto.end_at,tto.reason
FROM tutor_time_off tto
JOIN users u ON u.id=tto.tutor_id
WHERE u.email='pedro@peerly.study'
ORDER BY tto.start_at DESC;

SELECT s.id,s.title,s.starts_at,s.ends_at,s.status,s.meet_url
FROM sessions s
JOIN users u ON u.id=s.tutor_id
WHERE u.email='pedro@peerly.study'
ORDER BY s.starts_at;

SELECT s.id,s.title,s.starts_at,s.ends_at,s.status,s.meet_url
FROM sessions s
WHERE s.starts_at >= NOW()
ORDER BY s.starts_at
LIMIT 50;

SELECT s.id,s.title,s.starts_at,s.ends_at,s.status
FROM sessions s
JOIN session_participants sp ON sp.session_id=s.id
JOIN users u ON u.id=sp.user_id
WHERE u.email='larissa@peerly.study'
ORDER BY s.starts_at DESC;

SELECT s.id,s.title,s.status,s.meet_url
FROM session_participants sp
JOIN sessions s ON s.id=sp.session_id
JOIN users u ON u.id=sp.user_id
WHERE u.email='larissa@peerly.study'
ORDER BY s.starts_at DESC;

SELECT s.id,s.title,s.starts_at,s.ends_at,
       tu.full_name AS tutor_name, st.full_name AS student_name
FROM sessions s
JOIN users tu ON tu.id=s.tutor_id
LEFT JOIN session_participants sp ON sp.session_id=s.id
LEFT JOIN users st ON st.id=sp.user_id
WHERE s.status='scheduled'
ORDER BY s.starts_at;

SELECT s.id,s.title,s.starts_at,s.ends_at,s.status,s.meet_url
FROM sessions s
WHERE s.status='scheduled'
AND s.starts_at BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 7 DAY)
ORDER BY s.starts_at;

SELECT s.id,s.title,s.starts_at,s.ends_at,s.status
FROM sessions s
WHERE s.status IN ('finished','cancelled','no_show')
ORDER BY s.starts_at DESC
LIMIT 50;

SELECT s.id,s.title,s.starts_at,s.ends_at,
       TIMESTAMPDIFF(MINUTE,s.starts_at,s.ends_at) AS duration_minutes
FROM sessions s
ORDER BY duration_minutes DESC, s.starts_at DESC;

SELECT s.id,s.title,s.starts_at,s.ends_at,s.meet_url
FROM sessions s
WHERE s.meet_url IS NOT NULL AND s.meet_url <> ''
ORDER BY s.starts_at DESC;

SELECT m.sender_id,u.full_name,m.type,m.content,m.created_at
FROM messages m
LEFT JOIN users u ON u.id=m.sender_id
WHERE m.session_id=(
  SELECT id FROM sessions ORDER BY created_at DESC LIMIT 1
)
ORDER BY m.created_at;

SELECT m.id,m.session_id,m.sender_id,m.type,m.content,m.created_at
FROM messages m
WHERE m.session_id='PUT_SESSION_ID_AQUI'
ORDER BY m.created_at;

SELECT m.session_id,COUNT(*) AS total_messages
FROM messages m
GROUP BY m.session_id
ORDER BY total_messages DESC;

SELECT m.session_id,
       SUM(CASE WHEN m.sender_id IS NULL THEN 1 ELSE 0 END) AS system_messages,
       SUM(CASE WHEN m.sender_id IS NOT NULL THEN 1 ELSE 0 END) AS user_messages
FROM messages m
GROUP BY m.session_id
ORDER BY user_messages DESC;

SELECT r.id,r.session_id,r.rating,r.comment,r.created_at,
       st.full_name AS reviewer_name, tu.full_name AS tutor_name
FROM reviews r
JOIN users st ON st.id=r.reviewer_id
JOIN users tu ON tu.id=r.tutor_id
ORDER BY r.created_at DESC;

SELECT tu.id,tu.full_name,
       COUNT(r.id) AS reviews,
       ROUND(AVG(r.rating),2) AS avg_rating
FROM users tu
LEFT JOIN reviews r ON r.tutor_id=tu.id
WHERE tu.role IN ('tutor','both')
GROUP BY tu.id
HAVING reviews >= 1
ORDER BY avg_rating DESC, reviews DESC;

SELECT r.tutor_id,u.full_name,r.rating,r.comment,r.created_at
FROM reviews r
JOIN users u ON u.id=r.tutor_id
WHERE u.email='pedro@peerly.study'
ORDER BY r.created_at DESC;

SELECT n.title,n.body,n.created_at
FROM notifications n
JOIN users u ON u.id=n.user_id
WHERE u.email='larissa@peerly.study'
ORDER BY n.created_at DESC;

SELECT n.title,n.body,n.created_at
FROM notifications n
JOIN users u ON u.id=n.user_id
WHERE u.email='larissa@peerly.study' AND n.is_read=0
ORDER BY n.created_at DESC;

SELECT u.full_name,COUNT(n.id) AS unread_notifications
FROM users u
LEFT JOIN notifications n ON n.user_id=u.id AND n.is_read=0
GROUP BY u.id
ORDER BY unread_notifications DESC, u.full_name;

SELECT oa.provider,COUNT(*) AS total_accounts
FROM oauth_accounts oa
GROUP BY oa.provider
ORDER BY total_accounts DESC;

SELECT u.full_name,u.email,oa.provider,oa.provider_uid,oa.created_at
FROM oauth_accounts oa
JOIN users u ON u.id=oa.user_id
ORDER BY oa.created_at DESC;

SELECT s.id,s.title,s.starts_at,s.ends_at
FROM sessions s
WHERE NOT (s.ends_at <= '2025-12-14 09:00:00' OR s.starts_at >= '2025-12-14 10:00:00')
AND s.tutor_id=(SELECT id FROM users WHERE email='pedro@peerly.study')
ORDER BY s.starts_at;

SELECT u.id,u.full_name
FROM users u
WHERE u.role IN ('tutor','both')
AND EXISTS (
  SELECT 1
  FROM tutor_subjects ts
  JOIN subjects s ON s.id=ts.subject_id
  WHERE ts.tutor_id=u.id AND s.slug='matematica'
)
AND NOT EXISTS (
  SELECT 1
  FROM tutor_time_off tto
  WHERE tto.tutor_id=u.id
  AND '2025-12-14 09:00:00' < tto.end_at
  AND '2025-12-14 10:00:00' > tto.start_at
)
ORDER BY u.full_name;
