# Peerly REST API Documentation (Atualizada)

**Base URL (local dev):**
```txt
http://10.0.2.2:8080/api
```

Todas as rotas usam JSON (`Content-Type: application/json`) e seguem o padrão RESTful.

---

## Authentication

### POST `/auth/login`
Efetua login de um utilizador existente.

**Request Body**
```json
{
  "email": "rita@peerly.app",
  "password": "123456"
}
```

**Response 200**
```json
{
  "id": "baddd584-b456-11f0-95be-c4efbbb92864",
  "email": "rita@peerly.app",
  "fullName": "Ana Rita",
  "role": "tutor",
  "avatarUrl": "https://peerly.app/avatars/rita.jpg",
  "language": "pt"
}
```

**Erros**
| Código | Mensagem |
|---:|---|
| 400 | Campos em falta |
| 401 | Credenciais inválidas |
| 500 | Erro interno do servidor |

---

## Users

### GET `/users`
Lista todos os utilizadores registados.

**Response 200**
```json
[
  {
    "id": "2d238e82-bc00-11f0-a9b0-c4efbbb92864",
    "email": "pedro@peerly.app",
    "fullName": "Pedro Almeida",
    "role": "tutor",
    "avatarUrl": null,
    "language": "pt"
  }
]
```

### POST `/users`
Cria um novo utilizador.

**Request Body**
```json
{
  "email": "joao@peerly.app",
  "password": "123456",
  "fullName": "João Silva",
  "role": "student",
  "language": "pt"
}
```

**Response 201**
```json
{
  "id": "bade1da4-b456-11f0-95be-c4efbbb92864",
  "email": "joao@peerly.app",
  "fullName": "João Silva",
  "role": "student",
  "language": "pt",
  "createdAt": "2025-11-07T17:00:00"
}
```

### POST `/users/{id}/avatar`
Faz upload do avatar do utilizador.

**Request (multipart/form-data)**
| Campo | Tipo | Descrição |
|---|---|---|
| `file` | `binary` | Imagem JPEG ou PNG |

**Response 200**
```json
{
  "id": "baddd584-b456-11f0-95be-c4efbbb92864",
  "avatarUrl": "https://peerly.app/avatars/rita.jpg"
}
```

---

## Tutors

Atualmente partilham o mesmo modelo `users` com `role = "tutor"`.

### POST `/tutors/{id}/avatar`
Upload do avatar de um tutor.

**Request (multipart/form-data)**
| Campo | Tipo | Descrição |
|---|---|---|
| `file` | `binary` | Imagem JPEG/PNG |

**Response 200**
```json
{
  "url": "https://peerly.app/avatars/tutors/erica.jpg"
}
```

---

## Sessions

### GET `/sessions`
Lista todas as sessões agendadas.

**Response 200**
```json
[
  {
    "id": "5e9f2147-05a1-4eae-bf50-13f7dfc00221",
    "tutorId": "2d238e82-bc00-11f0-a9b0-c4efbbb92864",
    "subjectId": null,
    "title": "Sessão com Pedro Almeida — Matemática",
    "description": "Sessão agendada pela app Peerly.",
    "startsAt": "2025-11-07T09:00:00",
    "endsAt": "2025-11-07T10:00:00",
    "maxParticipants": 1,
    "priceTotalCents": null,
    "status": "scheduled",
    "meetUrl": null,
    "tutor": {
      "id": "2d238e82-bc00-11f0-a9b0-c4efbbb92864",
      "name": "Pedro Almeida",
      "avatarUrl": null
    }
  }
]
```

### GET `/sessions/{id}`
Obtém os detalhes de uma sessão específica.

**Response 200**
```json
{
  "id": "5e9f2147-05a1-4eae-bf50-13f7dfc00221",
  "tutorId": "baddd584-b456-11f0-95be-c4efbbb92864",
  "title": "Sessão com Ana Rita — Design",
  "description": "Sessão agendada pela app Peerly.",
  "startsAt": "2025-11-08T15:00:00",
  "endsAt": "2025-11-08T16:00:00",
  "status": "scheduled",
  "meetUrl": "https://meet.google.com/abc-defg-hij",
  "tutor": {
    "id": "baddd584-b456-11f0-95be-c4efbbb92864",
    "name": "Ana Rita",
    "avatarUrl": "https://peerly.app/avatars/rita.jpg"
  }
}
```

**Erro 404**
```json
{ "error": "Session not found" }
```

### POST `/sessions`
Cria uma nova sessão.

**Request Body**
```json
{
  "tutorId": "baddd584-b456-11f0-95be-c4efbbb92864",
  "subjectId": null,
  "title": "Sessão de UI/UX com Ana Rita",
  "description": "Sessão agendada pela app Peerly.",
  "startsAt": "2025-11-08T09:00:00",
  "endsAt": "2025-11-08T10:00:00",
  "maxParticipants": 1,
  "priceTotalCents": null,
  "studentId": "2d23944a-bc00-11f0-a9b0-c4efbbb92864"
}
```

**Response 200**
```json
{
  "id": "b0f4c0fa-6f2e-4c8e-b9a5-4f978d923fa2",
  "tutorId": "baddd584-b456-11f0-95be-c4efbbb92864",
  "title": "Sessão de UI/UX com Ana Rita",
  "startsAt": "2025-11-08T09:00:00",
  "endsAt": "2025-11-08T10:00:00",
  "status": "scheduled",
  "meetUrl": null
}
```

**Erros**
| Código | Mensagem |
|---:|---|
| 400 | Campos obrigatórios: tutorId, title, startsAt, endsAt |
| 500 | Internal Server Error (ex.: tutor inexistente) |

### DELETE `/sessions/{id}`
Remove uma sessão (caso seja cancelada).

**Response 204**
```txt
(no content)
```

---

## Meet

Esta secção cobre a geração/associação do link de Google Meet a uma sessão.

### POST `/meet/sessions/{sessionId}`
Gera (ou garante) um link de Meet para a sessão indicada.

Notas:
- Se a sessão já tiver `meetUrl`, o serviço pode devolver o mesmo link (idempotente do ponto de vista funcional).
- Normalmente usado pelo tutor (ou admin) para criar o link e partilhar no chat.

**Response 200**
```json
{
  "sessionId": "62c06a40-59cb-4327-aede-d7c9eed532b2",
  "meetUrl": "https://meet.google.com/mhd-hgri-htd"
}
```

**Erros**
| Código | Mensagem |
|---:|---|
| 404 | Sessão não encontrada |
| 500 | Erro interno (ex.: credenciais Google, Calendar/Meet API) |

---

## Messages

Mensagens associadas a uma sessão (chat em tempo real).

### GET `/sessions/{sessionId}/messages`
Lista mensagens da sessão (ordenadas por `createdAt` ascendente).

**Response 200**
```json
[
  {
    "id": "c9b1f320-3a5a-4f31-8b7d-44c6b78d86b9",
    "sessionId": "62c06a40-59cb-4327-aede-d7c9eed532b2",
    "senderId": "2d238e82-bc00-11f0-a9b0-c4efbbb92864",
    "type": "text",
    "content": "Pronta para aula?",
    "createdAt": "2025-12-14T11:14:10Z"
  },
  {
    "id": "cfb8c1f9-7c6b-4b7f-9c7b-3a1be32fe2e0",
    "sessionId": "62c06a40-59cb-4327-aede-d7c9eed532b2",
    "senderId": "2d238e82-bc00-11f0-a9b0-c4efbbb92864",
    "type": "system",
    "content": "Entra na videochamada: https://meet.google.com/mhd-hgri-htd",
    "createdAt": "2025-12-14T11:15:02Z"
  }
]
```

### POST `/sessions/{sessionId}/messages`
Envia uma mensagem para a sessão.

**Request Body**
```json
{
  "senderId": "2d238e82-bc00-11f0-a9b0-c4efbbb92864",
  "type": "text",
  "content": "Sim tutor!"
}
```

**Response 201**
```json
{
  "id": "3a9a48d2-0c6a-4a40-b93c-63c95d8c8f19",
  "sessionId": "62c06a40-59cb-4327-aede-d7c9eed532b2",
  "senderId": "2d238e82-bc00-11f0-a9b0-c4efbbb92864",
  "type": "text",
  "content": "Sim tutor!",
  "createdAt": "2025-12-14T11:16:21Z"
}
```

**Erros**
| Código | Mensagem |
|---:|---|
| 400 | Campos em falta (ex.: senderId/content) |
| 404 | Sessão não encontrada |
| 500 | Erro interno |

---

## Common Status Codes

| Código | Significado |
|---:|---|
| 200 | Sucesso |
| 201 | Criado com sucesso |
| 204 | Sem conteúdo (remoção bem-sucedida) |
| 400 | Requisição inválida |
| 401 | Não autorizado |
| 403 | Acesso negado |
| 404 | Não encontrado |
| 500 | Erro interno do servidor |

---

## Modelo de Dados (resumo)

| Entidade | Campos principais |
|---|---|
| **User** | id, email, fullName, passwordHash, role, avatarUrl, language |
| **Session** | id, tutorId, subjectId, title, description, startsAt, endsAt, status, meetUrl, maxParticipants, priceTotalCents |
| **Message** | id, sessionId, senderId, type (`text`/`system`/`file`), content, createdAt |
| **TutorSlimDto** | id, name, avatarUrl |
