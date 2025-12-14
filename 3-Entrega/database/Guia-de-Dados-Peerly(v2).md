# Guia de Dados – Plataforma Peerly

## 1. Introdução
Este Guia de Dados descreve, de forma detalhada, a base de dados da plataforma **Peerly**.  
O objetivo deste documento é servir como documentação técnica de apoio ao desenvolvimento, manutenção e avaliação académica do sistema, explicando a finalidade de cada tabela, os seus campos, tipos de dados e relações.

A base de dados foi concebida para suportar uma plataforma de tutoria académica, permitindo a gestão de utilizadores, tutores, sessões, mensagens, avaliações e integração com o Google Meet.

---

## 2. Convenções Gerais

- **SGBD:** MySQL / MariaDB  
- **Codificação:** UTF-8 (`utf8mb4`)  
- **Chaves primárias:** UUID (`VARCHAR(36)`)  
- **Datas e horas:** armazenadas em UTC  
- **Integridade referencial:** garantida através de chaves estrangeiras  

---

## 3. Tabela `users`

Armazena todos os utilizadores da plataforma (estudantes, tutores e administradores).

| Campo | Tipo | Descrição |
|------|------|-----------|
| id | VARCHAR(36) | Identificador único do utilizador |
| email | VARCHAR(254) | Email único |
| password_hash | TEXT | Hash da palavra-passe |
| full_name | VARCHAR(200) | Nome completo |
| role | ENUM | Perfil do utilizador (`student`, `tutor`, `both`, `admin`) |
| avatar_url | TEXT | URL do avatar |
| language | VARCHAR(10) | Idioma preferido |
| created_at | TIMESTAMP | Data de criação |
| updated_at | TIMESTAMP | Última atualização |

---

## 4. Tabela `oauth_accounts`

Guarda contas associadas a autenticação externa (OAuth).

| Campo | Tipo | Descrição |
|------|------|-----------|
| id | VARCHAR(36) | Identificador |
| user_id | VARCHAR(36) | Utilizador associado |
| provider | VARCHAR(40) | Provedor (ex.: Google) |
| provider_uid | VARCHAR(191) | Identificador externo |
| created_at | TIMESTAMP | Data de criação |

---

## 5. Tabela `subjects`

Lista das áreas/disciplinas disponíveis.

| Campo | Tipo | Descrição |
|------|------|-----------|
| id | VARCHAR(36) | Identificador |
| slug | VARCHAR(120) | Identificador textual |
| name | VARCHAR(120) | Nome da disciplina |

---

## 6. Tabela `tutor_subjects`

Relaciona tutores com disciplinas.

| Campo | Tipo | Descrição |
|------|------|-----------|
| tutor_id | VARCHAR(36) | Tutor |
| subject_id | VARCHAR(36) | Disciplina |
| level | VARCHAR(40) | Nível de ensino |
| price_per_hour_cents | INT | Preço por hora |

---

## 7. Tabela `sessions`

Representa sessões de tutoria.

| Campo | Tipo | Descrição |
|------|------|-----------|
| id | VARCHAR(36) | Identificador |
| tutor_id | VARCHAR(36) | Tutor |
| subject_id | VARCHAR(36) | Disciplina |
| title | VARCHAR(200) | Título |
| description | TEXT | Descrição |
| starts_at | DATETIME | Início |
| ends_at | DATETIME | Fim |
| status | ENUM | Estado da sessão |
| max_participants | SMALLINT | Máx. participantes |
| price_total_cents | INT | Preço total |
| meet_url | TEXT | Link do Google Meet |
| created_at | TIMESTAMP | Data de criação |

---

## 8. Tabela `session_participants`

Utilizadores inscritos numa sessão.

| Campo | Tipo | Descrição |
|------|------|-----------|
| session_id | VARCHAR(36) | Sessão |
| user_id | VARCHAR(36) | Utilizador |
| joined_at | TIMESTAMP | Data de entrada |

---

## 9. Tabela `messages`

Mensagens trocadas no chat da sessão.

| Campo | Tipo | Descrição |
|------|------|-----------|
| id | VARCHAR(36) | Identificador |
| session_id | VARCHAR(36) | Sessão |
| sender_id | VARCHAR(36) | Remetente |
| type | ENUM | Tipo (`text`, `system`, `file`) |
| content | MEDIUMTEXT | Conteúdo |
| created_at | TIMESTAMP | Data/hora |

---

## 10. Tabela `reviews`

Avaliações feitas pelos estudantes.

| Campo | Tipo | Descrição |
|------|------|-----------|
| id | VARCHAR(36) | Identificador |
| session_id | VARCHAR(36) | Sessão |
| reviewer_id | VARCHAR(36) | Estudante |
| tutor_id | VARCHAR(36) | Tutor |
| rating | TINYINT | Classificação (1–5) |
| comment | TEXT | Comentário |
| created_at | TIMESTAMP | Data |

---

## 11. Tabela `favorites`

Tutores marcados como favoritos.

| Campo | Tipo | Descrição |
|------|------|-----------|
| user_id | VARCHAR(36) | Utilizador |
| tutor_id | VARCHAR(36) | Tutor |
| created_at | TIMESTAMP | Data |

---

## 12. Tabela `notifications`

Notificações enviadas aos utilizadores.

| Campo | Tipo | Descrição |
|------|------|-----------|
| id | VARCHAR(36) | Identificador |
| user_id | VARCHAR(36) | Destinatário |
| title | VARCHAR(200) | Título |
| body | TEXT | Conteúdo |
| is_read | BOOLEAN | Estado |
| created_at | TIMESTAMP | Data |

---

## 13. Considerações Finais

Este Guia de Dados garante uma visão clara e estruturada da base de dados da Peerly, facilitando:
- manutenção do sistema,
- evolução futura,
- avaliação académica e técnica do projeto.

