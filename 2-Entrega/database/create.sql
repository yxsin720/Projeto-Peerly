-- Peerly - create.sql
-- Idempotent creation of database schema and objects (MySQL 8.0+)
-- Generated: 2025-11-08T21:56:05.307359

SET time_zone = '+00:00';

CREATE DATABASE IF NOT EXISTS peerly
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;
USE peerly;

SET NAMES utf8mb4 COLLATE utf8mb4_general_ci;
SET sql_mode = 'STRICT_ALL_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- =========================
-- Tables
-- =========================

CREATE TABLE IF NOT EXISTS users (
  id              VARCHAR(36) PRIMARY KEY,
  email           VARCHAR(254) NOT NULL UNIQUE,
  password_hash   TEXT NULL,
  full_name       VARCHAR(200) NOT NULL,
  role            ENUM('student','tutor','both','admin') NOT NULL DEFAULT 'student',
  avatar_url      TEXT NULL,
  language        VARCHAR(10) NOT NULL DEFAULT 'pt',
  created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_users_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS oauth_accounts (
  id            VARCHAR(36) PRIMARY KEY,
  user_id       VARCHAR(36) NOT NULL,
  provider      VARCHAR(40) NOT NULL,       -- 'google' | 'apple' | etc.
  provider_uid  VARCHAR(191) NOT NULL,
  created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uq_provider (provider, provider_uid),
  CONSTRAINT fk_oauth_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS subjects (
  id     VARCHAR(36) PRIMARY KEY,
  slug   VARCHAR(120) NOT NULL UNIQUE,
  name   VARCHAR(120) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS tutor_subjects (
  tutor_id             VARCHAR(36) NOT NULL,
  subject_id           VARCHAR(36) NOT NULL,
  level                VARCHAR(40) NULL,         -- "iniciante","intermédio","avançado"
  price_per_hour_cents INT NULL,
  PRIMARY KEY (tutor_id, subject_id),
  CONSTRAINT fk_ts_tutor   FOREIGN KEY (tutor_id)   REFERENCES users(id)    ON DELETE CASCADE,
  CONSTRAINT fk_ts_subject FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS tutor_availability (
  id         VARCHAR(36) PRIMARY KEY,
  tutor_id   VARCHAR(36) NOT NULL,
  weekday    TINYINT NOT NULL,     -- 0..6 (0=Sunday)
  start_time TIME NOT NULL,
  end_time   TIME NOT NULL,
  timezone   VARCHAR(64) NOT NULL DEFAULT 'UTC',
  CONSTRAINT chk_weekday CHECK (weekday BETWEEN 0 AND 6),
  CONSTRAINT chk_time_range CHECK (start_time < end_time),
  CONSTRAINT fk_ta_tutor FOREIGN KEY (tutor_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS tutor_time_off (
  id        VARCHAR(36) PRIMARY KEY,
  tutor_id  VARCHAR(36) NOT NULL,
  start_at  DATETIME NOT NULL,     -- UTC
  end_at    DATETIME NOT NULL,     -- UTC
  reason    VARCHAR(255),
  CONSTRAINT chk_timeoff_range CHECK (start_at < end_at),
  CONSTRAINT fk_tto_tutor FOREIGN KEY (tutor_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS sessions (
  id                VARCHAR(36) PRIMARY KEY,
  tutor_id          VARCHAR(36) NOT NULL,
  subject_id        VARCHAR(36) NULL,
  title             VARCHAR(200) NOT NULL,   -- "Matemática com Pedro"
  description       TEXT NULL,
  starts_at         DATETIME NOT NULL,       -- UTC
  ends_at           DATETIME NOT NULL,       -- UTC
  status            ENUM('scheduled','live','finished','cancelled','no_show') NOT NULL DEFAULT 'scheduled',
  max_participants  SMALLINT NOT NULL DEFAULT 1,
  price_total_cents INT NULL,
  created_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT chk_session_range CHECK (starts_at < ends_at),
  CONSTRAINT fk_s_tutor   FOREIGN KEY (tutor_id)  REFERENCES users(id)    ON DELETE RESTRICT,
  CONSTRAINT fk_s_subject FOREIGN KEY (subject_id)REFERENCES subjects(id) ON DELETE SET NULL,
  INDEX idx_sessions_tutor_time (tutor_id, starts_at),
  INDEX idx_sessions_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS session_participants (
  session_id  VARCHAR(36) NOT NULL,
  user_id     VARCHAR(36) NOT NULL,
  joined_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (session_id, user_id),
  CONSTRAINT fk_sp_session FOREIGN KEY (session_id) REFERENCES sessions(id) ON DELETE CASCADE,
  CONSTRAINT fk_sp_user    FOREIGN KEY (user_id)    REFERENCES users(id)    ON DELETE CASCADE,
  INDEX idx_participants_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS messages (
  id          VARCHAR(36) PRIMARY KEY,
  session_id  VARCHAR(36) NOT NULL,
  sender_id   VARCHAR(36) NULL,
  type        ENUM('text','system','file') NOT NULL DEFAULT 'text',
  content     MEDIUMTEXT,
  created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_m_session FOREIGN KEY (session_id) REFERENCES sessions(id) ON DELETE CASCADE,
  CONSTRAINT fk_m_sender  FOREIGN KEY (sender_id)  REFERENCES users(id)    ON DELETE SET NULL,
  INDEX idx_messages_session_time (session_id, created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS reviews (
  id           VARCHAR(36) PRIMARY KEY,
  session_id   VARCHAR(36) NOT NULL,
  reviewer_id  VARCHAR(36) NOT NULL,     -- aluno
  tutor_id     VARCHAR(36) NOT NULL,
  rating       TINYINT NOT NULL,         -- 1..5 (validar na app)
  comment      TEXT,
  created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uq_review_once (session_id, reviewer_id),
  CONSTRAINT chk_rating CHECK (rating BETWEEN 1 AND 5),
  CONSTRAINT fk_r_session  FOREIGN KEY (session_id)  REFERENCES sessions(id) ON DELETE CASCADE,
  CONSTRAINT fk_r_reviewer FOREIGN KEY (reviewer_id) REFERENCES users(id)    ON DELETE CASCADE,
  CONSTRAINT fk_r_tutor    FOREIGN KEY (tutor_id)    REFERENCES users(id)    ON DELETE CASCADE,
  INDEX idx_reviews_tutor (tutor_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS favorites (
  user_id    VARCHAR(36) NOT NULL,
  tutor_id   VARCHAR(36) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (user_id, tutor_id),
  CONSTRAINT fk_f_user  FOREIGN KEY (user_id)  REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT fk_f_tutor FOREIGN KEY (tutor_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS notifications (
  id         VARCHAR(36) PRIMARY KEY,
  user_id    VARCHAR(36) NOT NULL,
  title      VARCHAR(200) NOT NULL,
  body       TEXT,
  is_read    TINYINT(1) NOT NULL DEFAULT 0,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_n_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- =========================
-- Triggers (UUID defaults)
-- =========================
DROP TRIGGER IF EXISTS trg_users_uuid;
CREATE TRIGGER trg_users_uuid BEFORE INSERT ON users
FOR EACH ROW SET NEW.id = IFNULL(NULLIF(NEW.id,''), UUID());

DROP TRIGGER IF EXISTS trg_oauth_uuid;
CREATE TRIGGER trg_oauth_uuid BEFORE INSERT ON oauth_accounts
FOR EACH ROW SET NEW.id = IFNULL(NULLIF(NEW.id,''), UUID());

DROP TRIGGER IF EXISTS trg_subjects_uuid;
CREATE TRIGGER trg_subjects_uuid BEFORE INSERT ON subjects
FOR EACH ROW SET NEW.id = IFNULL(NULLIF(NEW.id,''), UUID());

DROP TRIGGER IF EXISTS trg_ta_uuid;
CREATE TRIGGER trg_ta_uuid BEFORE INSERT ON tutor_availability
FOR EACH ROW SET NEW.id = IFNULL(NULLIF(NEW.id,''), UUID());

DROP TRIGGER IF EXISTS trg_tto_uuid;
CREATE TRIGGER trg_tto_uuid BEFORE INSERT ON tutor_time_off
FOR EACH ROW SET NEW.id = IFNULL(NULLIF(NEW.id,''), UUID());

DROP TRIGGER IF EXISTS trg_sessions_uuid;
CREATE TRIGGER trg_sessions_uuid BEFORE INSERT ON sessions
FOR EACH ROW SET NEW.id = IFNULL(NULLIF(NEW.id,''), UUID());

DROP TRIGGER IF EXISTS trg_messages_uuid;
CREATE TRIGGER trg_messages_uuid BEFORE INSERT ON messages
FOR EACH ROW SET NEW.id = IFNULL(NULLIF(NEW.id,''), UUID());

DROP TRIGGER IF EXISTS trg_reviews_uuid;
CREATE TRIGGER trg_reviews_uuid BEFORE INSERT ON reviews
FOR EACH ROW SET NEW.id = IFNULL(NULLIF(NEW.id,''), UUID());

DROP TRIGGER IF EXISTS trg_notifications_uuid;
CREATE TRIGGER trg_notifications_uuid BEFORE INSERT ON notifications
FOR EACH ROW SET NEW.id = IFNULL(NULLIF(NEW.id,''), UUID());

-- =========================
-- Views
-- =========================
DROP VIEW IF EXISTS tutor_rating;
CREATE VIEW tutor_rating AS
SELECT
  t.id AS tutor_id,
  COALESCE(ROUND(AVG(r.rating), 2), 0) AS avg_rating,
  COUNT(r.id) AS reviews_count
FROM users t
LEFT JOIN reviews r ON r.tutor_id = t.id
GROUP BY t.id;

-- =========================
-- Helper indexes (optional)
-- =========================
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_subjects_slug ON subjects(slug);

-- Done.
