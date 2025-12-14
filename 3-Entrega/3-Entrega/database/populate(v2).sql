INSERT INTO users (id,email,password_hash,full_name,role,avatar_url,language,area)
VALUES
(UUID(),'pedro@peerly.study','x','Pedro Almeida','tutor',NULL,'pt','Matemática'),
(UUID(),'erica@peerly.study','x','ERICA SANTOS','tutor',NULL,'pt',NULL),
(UUID(),'rita@peerly.study',NULL,'Rita Fernandes','tutor',NULL,'pt',NULL),
(UUID(),'joao@peerly.study',NULL,'João Silva','tutor',NULL,'pt',NULL),
(UUID(),'francisco@peerly.study','x','Francisco Lima','student',NULL,'pt',NULL),
(UUID(),'larissa@peerly.study','1','Larissa Almeida','student',NULL,'pt','Design');

INSERT INTO subjects (id,slug,name)
VALUES
(UUID(),'matematica','Matemática'),
(UUID(),'programacao','Programação'),
(UUID(),'design','Design');

INSERT INTO tutor_subjects (tutor_id,subject_id,level,price_per_hour_cents)
SELECT u.id,s.id,'intermédio',2000
FROM users u, subjects s
WHERE u.email='pedro@peerly.study' AND s.slug='matematica';

INSERT INTO sessions (
  id,tutor_id,subject_id,title,starts_at,ends_at,meet_url,status
)
SELECT
  UUID(),
  u.id,
  s.id,
  'Matemática com Pedro',
  '2025-12-14 09:00:00',
  '2025-12-14 10:00:00',
  'https://meet.google.com/abc-defg-hij',
  'scheduled'
FROM users u, subjects s
WHERE u.email='pedro@peerly.study' AND s.slug='matematica';

INSERT INTO session_participants (session_id,user_id)
SELECT s.id,u.id
FROM sessions s, users u
WHERE u.email='larissa@peerly.study';

INSERT INTO messages (id,session_id,sender_id,content)
SELECT UUID(),s.id,u.id,'Entra na videochamada: https://meet.google.com/abc-defg-hij'
FROM sessions s, users u
WHERE u.email='pedro@peerly.study';
