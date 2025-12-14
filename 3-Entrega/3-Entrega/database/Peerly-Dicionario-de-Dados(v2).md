# Peerly — Dicionário de Dados (Modelo ER) — Versão Atualizada

> Documento académico: descrição das entidades, atributos e relações do modelo Entidade–Relação (ER) da base de dados **Peerly**.  
> Data: 2025-12-14

---

## 1. Convenções

- **PK**: chave primária  
- **FK**: chave estrangeira  
- Identificadores: `VARCHAR(36)` (UUID em formato texto)  
- Datas: `TIMESTAMP` (criação/actualização) e `DATETIME` (início/fim de eventos)  
- Valores monetários: `*_cents` (inteiro em cêntimos)  
- Campos opcionais são indicados como “(opcional)”.

---

## 2. Visão geral do modelo ER

O modelo ER da Peerly organiza-se em torno de:
- **Utilizadores** (`users`) e autenticação externa (`oauth_accounts`);
- **Catálogo de disciplinas** (`subjects`) e associação de tutores a disciplinas (`tutor_subjects`);
- **Sessões de tutoria** (`sessions`) e respectivos **participantes** (`session_participants`);
- **Chat** associado a cada sessão (`messages`) e geração de **links de Google Meet** (guardado em `sessions` e/ou enviado por mensagem);
- **Avaliações** pós-sessão (`reviews`);
- Funcionalidades sociais: **favoritos** (`favorites`) e **notificações** (`notifications`);
- Gestão de indisponibilidades do tutor (`tutor_time_off`).

---

## 3. Tabela `users`

Armazena utilizadores (alunos e/ou tutores).

| Campo | Tipo | Descrição |
|------|------|-----------|
| id | VARCHAR(36) | **PK**. Identificador do utilizador |
| name | VARCHAR(120) | Nome completo |
| email | VARCHAR(160) | Email (único) |
| password_hash | VARCHAR(255) | Hash da palavra‑passe (quando login local) |
| role | ENUM('student','tutor','both') | Perfil na plataforma |
| area | VARCHAR(80) | Área/curso (opcional) |
| bio | TEXT | Descrição/perfil (opcional) |
| avatar_url | TEXT | URL do avatar (opcional) |
| rating_avg | DECIMAL(3,2) | Média de avaliações (opcional/derivável) |
| rating_count | INT | Nº de avaliações (opcional/derivável) |
| created_at | TIMESTAMP | Data de criação |
| updated_at | TIMESTAMP | Data de actualização |

**Índices/Restrições**
- `UNIQUE(email)`

---

## 4. Tabela `oauth_accounts`

Liga um utilizador a um provedor OAuth (Google/Facebook, etc.).

| Campo | Tipo | Descrição |
|------|------|-----------|
| id | VARCHAR(36) | **PK** |
| user_id | VARCHAR(36) | **FK → users.id** |
| provider | ENUM('google','facebook','apple') | Provedor |
| provider_user_id | VARCHAR(200) | ID do utilizador no provedor |
| email | VARCHAR(160) | Email devolvido pelo provedor (opcional) |
| created_at | TIMESTAMP | Criação |
| updated_at | TIMESTAMP | Actualização |

**Índices/Restrições**
- `UNIQUE(provider, provider_user_id)`

---

## 5. Tabela `subjects`

Catálogo de disciplinas/áreas de tutoria.

| Campo | Tipo | Descrição |
|------|------|-----------|
| id | VARCHAR(36) | **PK** |
| name | VARCHAR(120) | Nome da disciplina (único) |
| description | TEXT | Descrição (opcional) |
| created_at | TIMESTAMP | Criação |
| updated_at | TIMESTAMP | Actualização |

**Índices/Restrições**
- `UNIQUE(name)`

---

## 6. Tabela `tutor_subjects`

Relação N:N entre tutores e disciplinas.

| Campo | Tipo | Descrição |
|------|------|-----------|
| tutor_id | VARCHAR(36) | **PK/FK → users.id** (tutor) |
| subject_id | VARCHAR(36) | **PK/FK → subjects.id** |
| level | VARCHAR(40) | Nível (ex.: “Básico”, “Avançado”) (opcional) |
| hourly_rate_cents | INT | Preço/hora (opcional) |
| created_at | TIMESTAMP | Criação |

**Índices/Restrições**
- `PRIMARY KEY (tutor_id, subject_id)`

---

## 7. Tabela `tutor_time_off`

Períodos de indisponibilidade de um tutor (ex.: férias, exames, bloqueios).

| Campo | Tipo | Descrição |
|------|------|-----------|
| id | VARCHAR(36) | **PK** |
| tutor_id | VARCHAR(36) | **FK → users.id** |
| starts_at | DATETIME | Início da indisponibilidade |
| ends_at | DATETIME | Fim da indisponibilidade |
| reason | VARCHAR(200) | Motivo (opcional) |
| created_at | TIMESTAMP | Criação |
| updated_at | TIMESTAMP | Actualização |

---

## 8. Tabela `sessions`

Representa sessões de tutoria criadas por tutores.

| Campo | Tipo | Descrição |
|------|------|-----------|
| id | VARCHAR(36) | **PK** |
| tutor_id | VARCHAR(36) | **FK → users.id** (tutor dono da sessão) |
| subject_id | VARCHAR(36) | **FK → subjects.id** |
| title | VARCHAR(200) | Título (opcional) |
| description | TEXT | Descrição (opcional) |
| starts_at | DATETIME | Data/hora de início |
| ends_at | DATETIME | Data/hora de fim |
| status | ENUM('scheduled','ongoing','finished','cancelled') | Estado |
| max_participants | SMALLINT | Nº máximo de participantes |
| price_total_cents | INT | Preço total (opcional) |

### Campos de videoconferência (Google Meet)
| Campo | Tipo | Descrição |
|------|------|-----------|
| meet_url | TEXT | Link do Google Meet (opcional) |
| meet_space_name | VARCHAR(120) | `spaces/...` devolvido pela API (opcional) |
| meet_created_at | TIMESTAMP | Data de geração do Meet (opcional) |

### Auditoria
| Campo | Tipo | Descrição |
|------|------|-----------|
| created_at | TIMESTAMP | Criação |
| updated_at | TIMESTAMP | Actualização |

---

## 9. Tabela `session_participants`

Utilizadores inscritos numa sessão (N:N).

| Campo | Tipo | Descrição |
|------|------|-----------|
| session_id | VARCHAR(36) | **PK/FK → sessions.id** |
| user_id | VARCHAR(36) | **PK/FK → users.id** |
| joined_at | TIMESTAMP | Data de entrada |
| role_in_session | ENUM('student','tutor') | Papel na sessão (opcional, derivável) |

**Índices/Restrições**
- `PRIMARY KEY (session_id, user_id)`

---

## 10. Tabela `messages`

Mensagens do chat de uma sessão.

| Campo | Tipo | Descrição |
|------|------|-----------|
| id | VARCHAR(36) | **PK** |
| session_id | VARCHAR(36) | **FK → sessions.id** |
| sender_id | VARCHAR(36) | **FK → users.id** |
| content | TEXT | Conteúdo da mensagem |
| message_type | ENUM('text','system') | Tipo (opcional) |
| created_at | TIMESTAMP | Data/hora de envio |

**Notas**
- O link do Meet pode ser enviado como mensagem (ex.: “Entra na videochamada: …”).  
- A aplicação pode destacar o link mais recente (UX), mas a BD guarda todas as mensagens.

---

## 11. Tabela `reviews`

Avaliações após a sessão (normalmente feitas por alunos ao tutor).

| Campo | Tipo | Descrição |
|------|------|-----------|
| id | VARCHAR(36) | **PK** |
| session_id | VARCHAR(36) | **FK → sessions.id** |
| reviewer_id | VARCHAR(36) | **FK → users.id** (quem avalia) |
| tutor_id | VARCHAR(36) | **FK → users.id** (quem é avaliado) |
| rating | TINYINT | 1–5 |
| comment | TEXT | Comentário (opcional) |
| created_at | TIMESTAMP | Criação |

**Índices/Restrições**
- Recomendado: `UNIQUE(session_id, reviewer_id)` para impedir duplicados

---

## 12. Tabela `favorites`

Utilizadores que um aluno marcou como favorito (tutores).

| Campo | Tipo | Descrição |
|------|------|-----------|
| user_id | VARCHAR(36) | **PK/FK → users.id** (quem marca) |
| tutor_id | VARCHAR(36) | **PK/FK → users.id** (tutor favorito) |
| created_at | TIMESTAMP | Criação |

**Índices/Restrições**
- `PRIMARY KEY (user_id, tutor_id)`

---

## 13. Tabela `notifications`

Notificações internas (ex.: sessão confirmada, sessão a começar, etc.).

| Campo | Tipo | Descrição |
|------|------|-----------|
| id | VARCHAR(36) | **PK** |
| user_id | VARCHAR(36) | **FK → users.id** (destinatário) |
| title | VARCHAR(120) | Título |
| body | TEXT | Mensagem |
| type | VARCHAR(60) | Tipo/categoria (opcional) |
| is_read | BOOLEAN | Lida? |
| created_at | TIMESTAMP | Criação |

---

## 14. Relações principais (resumo)

- `users (1) ── (N) sessions` via `sessions.tutor_id`  
- `subjects (1) ── (N) sessions` via `sessions.subject_id`  
- `users (N) ── (N) sessions` via `session_participants`  
- `sessions (1) ── (N) messages` via `messages.session_id`  
- `users (1) ── (N) messages` via `messages.sender_id`  
- `sessions (1) ── (N) reviews` via `reviews.session_id`  
- `users (1) ── (N) reviews` (reviewer e tutor)  
- `users (N) ── (N) subjects` (tutores) via `tutor_subjects`  
- `users (1) ── (N) tutor_time_off` via `tutor_time_off.tutor_id`  
- `users (N) ── (N) users` (favoritos) via `favorites`

---

## 15. Notas de implementação

- Se a aplicação gerar um novo Meet para a mesma sessão, recomenda‑se actualizar `sessions.meet_url` e guardar o histórico via `messages` (mensagem “system” ou “text”).  
- Campos “deriváveis” (ex.: `rating_avg`, `rating_count`) podem ser mantidos por performance, mas também podem ser calculados a partir de `reviews`.

