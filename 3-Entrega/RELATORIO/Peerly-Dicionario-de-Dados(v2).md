# Peerly — Dicionário de Dados (Atualizado)

> Atualização pedida: **adicionado apenas `meet_url` na tabela `sessions`**.

## Convenções

- **PK**: Primary Key  
- **FK**: Foreign Key  
- Datas/horas:
  - `TIMESTAMP`: geralmente auto-gerido pelo MySQL (`CURRENT_TIMESTAMP`)
  - `DATETIME`: usado para instantes em UTC quando indicado no modelo
- Charset/Collation: `utf8mb4 / utf8mb4_general_ci`
- UUIDs: `VARCHAR(36)` (gerados por triggers quando o `id` vem vazio)

---

## `users`

| Campo | Tipo | Nulo | Chave | Default | Descrição |
|---|---|---:|---|---|---|
| id | VARCHAR(36) | Não | PK | (trigger UUID) | Identificador do utilizador (UUID) |
| email | VARCHAR(254) | Não | UNIQUE | — | Email único do utilizador |
| password_hash | TEXT | Sim | — | NULL | Hash da palavra-passe (pode ser NULL em OAuth) |
| full_name | VARCHAR(200) | Não | — | — | Nome completo |
| role | ENUM('student','tutor','both','admin') | Não | INDEX | 'student' | Perfil do utilizador |
| avatar_url | TEXT | Sim | — | NULL | URL do avatar |
| language | VARCHAR(10) | Não | — | 'pt' | Idioma (ex.: `pt`, `en`) |
| created_at | TIMESTAMP | Não | — | CURRENT_TIMESTAMP | Data de criação |
| updated_at | TIMESTAMP | Não | — | CURRENT_TIMESTAMP ON UPDATE | Data de atualização |

**Índices**
- `idx_users_role (role)`
- `idx_users_email (email)` *(helper index)*

---

## `oauth_accounts`

| Campo | Tipo | Nulo | Chave | Default | Descrição |
|---|---|---:|---|---|---|
| id | VARCHAR(36) | Não | PK | (trigger UUID) | Identificador do registo OAuth |
| user_id | VARCHAR(36) | Não | FK → users.id | — | Utilizador associado |
| provider | VARCHAR(40) | Não | — | — | Provider (ex.: `google`, `apple`) |
| provider_uid | VARCHAR(191) | Não | UNIQUE (provider, provider_uid) | — | ID do utilizador no provider |
| created_at | TIMESTAMP | Não | — | CURRENT_TIMESTAMP | Data de criação |

**Regras**
- `ON DELETE CASCADE` em `user_id`

---

## `subjects`

| Campo | Tipo | Nulo | Chave | Default | Descrição |
|---|---|---:|---|---|---|
| id | VARCHAR(36) | Não | PK | (trigger UUID) | Identificador da disciplina |
| slug | VARCHAR(120) | Não | UNIQUE | — | Código/slug (ex.: `matematica`) |
| name | VARCHAR(120) | Não | — | — | Nome (ex.: `Matemática`) |

**Índices**
- `idx_subjects_slug (slug)` *(helper index)*

---

## `tutor_subjects`

Tabela de relação **N:N** entre tutores (users com role tutor/both) e disciplinas.

| Campo | Tipo | Nulo | Chave | Default | Descrição |
|---|---|---:|---|---|---|
| tutor_id | VARCHAR(36) | Não | PK/FK → users.id | — | Tutor |
| subject_id | VARCHAR(36) | Não | PK/FK → subjects.id | — | Disciplina |
| level | VARCHAR(40) | Sim | — | NULL | Nível (ex.: iniciante/intermédio/avançado) |
| price_per_hour_cents | INT | Sim | — | NULL | Preço por hora (em cêntimos) |

**Regras**
- `ON DELETE CASCADE` em `tutor_id` e `subject_id`

---

## `tutor_availability`

Disponibilidade semanal do tutor.

| Campo | Tipo | Nulo | Chave | Default | Descrição |
|---|---|---:|---|---|---|
| id | VARCHAR(36) | Não | PK | (trigger UUID) | Identificador do bloco |
| tutor_id | VARCHAR(36) | Não | FK → users.id | — | Tutor |
| weekday | TINYINT | Não | CHECK 0..6 | — | Dia da semana (0=Domingo ... 6=Sábado) |
| start_time | TIME | Não | CHECK start<end | — | Hora de início |
| end_time | TIME | Não | CHECK start<end | — | Hora de fim |
| timezone | VARCHAR(64) | Não | — | 'UTC' | Timezone (ex.: `Europe/Lisbon`) |

**Regras**
- `ON DELETE CASCADE` em `tutor_id`

---

## `tutor_time_off`

Períodos de indisponibilidade (ex.: férias).

| Campo | Tipo | Nulo | Chave | Default | Descrição |
|---|---|---:|---|---|---|
| id | VARCHAR(36) | Não | PK | (trigger UUID) | Identificador do bloqueio |
| tutor_id | VARCHAR(36) | Não | FK → users.id | — | Tutor |
| start_at | DATETIME | Não | CHECK start<end | — | Início (UTC) |
| end_at | DATETIME | Não | CHECK start<end | — | Fim (UTC) |
| reason | VARCHAR(255) | Sim | — | NULL | Motivo opcional |

**Regras**
- `ON DELETE CASCADE` em `tutor_id`

---

## `sessions` (ATUALIZADA)

Sessões de tutoria agendadas.

| Campo | Tipo | Nulo | Chave | Default | Descrição |
|---|---|---:|---|---|---|
| id | VARCHAR(36) | Não | PK | (trigger UUID) | Identificador da sessão |
| tutor_id | VARCHAR(36) | Não | FK → users.id | — | Tutor responsável |
| subject_id | VARCHAR(36) | Sim | FK → subjects.id | NULL | Disciplina (opcional) |
| title | VARCHAR(200) | Não | — | — | Título (ex.: “Matemática com Pedro”) |
| description | TEXT | Sim | — | NULL | Descrição |
| starts_at | DATETIME | Não | CHECK start<end | — | Início (UTC) |
| ends_at | DATETIME | Não | CHECK start<end | — | Fim (UTC) |
| status | ENUM('scheduled','live','finished','cancelled','no_show') | Não | INDEX | 'scheduled' | Estado |
| max_participants | SMALLINT | Não | — | 1 | Máximo de participantes |
| price_total_cents | INT | Sim | — | NULL | Preço total (cêntimos) |
| meet_url | TEXT | Sim | — | NULL | **Link do Google Meet** associado à sessão |
| created_at | TIMESTAMP | Não | — | CURRENT_TIMESTAMP | Data de criação |

**Índices**
- `idx_sessions_tutor_time (tutor_id, starts_at)`
- `idx_sessions_status (status)`

**Regras**
- `tutor_id`: `ON DELETE RESTRICT`
- `subject_id`: `ON DELETE SET NULL`

---

## `session_participants`

Participantes ligados a uma sessão (permite 1 ou vários).

| Campo | Tipo | Nulo | Chave | Default | Descrição |
|---|---|---:|---|---|---|
| session_id | VARCHAR(36) | Não | PK/FK → sessions.id | — | Sessão |
| user_id | VARCHAR(36) | Não | PK/FK → users.id | — | Utilizador |
| joined_at | TIMESTAMP | Não | — | CURRENT_TIMESTAMP | Data/hora de entrada |

**Índices**
- `idx_participants_user (user_id)`

---

## `messages`

Mensagens do chat por sessão.

| Campo | Tipo | Nulo | Chave | Default | Descrição |
|---|---|---:|---|---|---|
| id | VARCHAR(36) | Não | PK | (trigger UUID) | Identificador da mensagem |
| session_id | VARCHAR(36) | Não | FK → sessions.id | — | Sessão |
| sender_id | VARCHAR(36) | Sim | FK → users.id | NULL | Remetente (NULL para sistema) |
| type | ENUM('text','system','file') | Não | — | 'text' | Tipo de mensagem |
| content | MEDIUMTEXT | Sim | — | NULL | Conteúdo |
| created_at | TIMESTAMP | Não | INDEX | CURRENT_TIMESTAMP | Data/hora |

**Índices**
- `idx_messages_session_time (session_id, created_at)`

---

## `reviews`

Avaliações do tutor após sessão.

| Campo | Tipo | Nulo | Chave | Default | Descrição |
|---|---|---:|---|---|---|
| id | VARCHAR(36) | Não | PK | (trigger UUID) | Identificador da review |
| session_id | VARCHAR(36) | Não | FK → sessions.id | — | Sessão |
| reviewer_id | VARCHAR(36) | Não | FK → users.id | — | Quem avalia (normalmente aluno) |
| tutor_id | VARCHAR(36) | Não | FK → users.id | — | Tutor avaliado |
| rating | TINYINT | Não | CHECK 1..5 | — | Nota 1 a 5 |
| comment | TEXT | Sim | — | NULL | Comentário |
| created_at | TIMESTAMP | Não | — | CURRENT_TIMESTAMP | Data |

**Regras**
- `UNIQUE (session_id, reviewer_id)` (1 review por pessoa por sessão)
- `idx_reviews_tutor (tutor_id)`

---

## `favorites`

Lista de tutores favoritos por utilizador.

| Campo | Tipo | Nulo | Chave | Default | Descrição |
|---|---|---:|---|---|---|
| user_id | VARCHAR(36) | Não | PK/FK → users.id | — | Utilizador |
| tutor_id | VARCHAR(36) | Não | PK/FK → users.id | — | Tutor favorito |
| created_at | TIMESTAMP | Não | — | CURRENT_TIMESTAMP | Data |

---

## `notifications`

Notificações guardadas.

| Campo | Tipo | Nulo | Chave | Default | Descrição |
|---|---|---:|---|---|---|
| id | VARCHAR(36) | Não | PK | (trigger UUID) | Identificador |
| user_id | VARCHAR(36) | Não | FK → users.id | — | Utilizador |
| title | VARCHAR(200) | Não | — | — | Título |
| body | TEXT | Sim | — | NULL | Texto |
| is_read | TINYINT(1) | Não | — | 0 | Lida? (0/1) |
| created_at | TIMESTAMP | Não | — | CURRENT_TIMESTAMP | Data |

---

## View: `tutor_rating`

Resumo de rating por tutor (calculado a partir de `reviews`).

| Campo | Tipo | Descrição |
|---|---|---|
| tutor_id | VARCHAR(36) | ID do tutor |
| avg_rating | NUM (ROUND(AVG)) | Média arredondada (2 casas) |
| reviews_count | NUM (COUNT) | Nº de reviews |

---

## Patch SQL (apenas adicionar `meet_url`)

```sql
ALTER TABLE sessions
  ADD COLUMN meet_url TEXT NULL AFTER price_total_cents;
```
