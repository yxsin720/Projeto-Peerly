#  Peerly REST API Documentation

**Base URL (local dev):**
```
http://10.0.2.2:8080/api
```

> Todas as rotas usam JSON (`Content-Type: application/json`)  
> e seguem o padrão RESTful.

---

##  AUTHENTICATION

### POST `/auth/login`
Efetua login de um utilizador existente.

#### Request Body
```json
{
  "email": "rita@peerly.app",
  "password": "123456"
}
```

#### Response 200
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

#### Error Responses
| Código | Mensagem |
|--------|-----------|
| 400 | Campos em falta |
| 401 | Credenciais inválidas |
| 500 | Erro interno do servidor |

---

##  USERS

### GET `/users`
Lista todos os utilizadores registados.

#### Response 200
```json
[
  {
    "id": "2d238e82-bc00-11f0-a9b0-c4efbbb92864",
    "email": "pedro@peerly.app",
    "fullName": "Pedro Almeida",
    "role": "tutor",
    "avatarUrl": null,
    "language": "pt"
  },
  {
    "id": "baddd584-b456-11f0-95be-c4efbbb92864",
    "email": "rita@peerly.app",
    "fullName": "Ana Rita",
    "role": "tutor",
    "avatarUrl": "https://peerly.app/avatars/rita.jpg",
    "language": "pt"
  }
]
```

---

### POST `/users`
Cria um novo utilizador.

#### Request Body
```json
{
  "email": "joao@peerly.app",
  "password": "123456",
  "fullName": "João Silva",
  "role": "student",
  "language": "pt"
}
```

#### Response 201
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

---

### POST `/users/{id}/avatar`
Faz upload do avatar do utilizador.

#### Request (multipart/form-data)
| Campo | Tipo | Descrição |
|--------|------|-----------|
| `file` | `binary` | Imagem JPEG ou PNG |

#### Response 200
```json
{
  "id": "baddd584-b456-11f0-95be-c4efbbb92864",
  "avatarUrl": "https://peerly.app/avatars/rita.jpg"
}
```

---

##  TUTORS

> Atualmente partilham o mesmo modelo `users` com `role = "tutor"`,  
> mas é possível expandir para tabela própria `tutors`.

### POST `/tutors/{id}/avatar`
Upload do avatar de um tutor.

#### Request
| Campo | Tipo | Descrição |
|--------|------|-----------|
| `file` | `binary` | Imagem JPEG/PNG |

#### Response
```json
{
  "url": "https://peerly.app/avatars/tutors/erica.jpg"
}
```

---

##  SESSIONS

### GET `/sessions`
Lista todas as sessões agendadas.

#### Response 200
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
    "tutor": {
      "id": "2d238e82-bc00-11f0-a9b0-c4efbbb92864",
      "name": "Pedro Almeida",
      "avatarUrl": null
    }
  }
]
```

---

### GET `/sessions/{id}`
Obtém os detalhes de uma sessão específica.

#### Response 200
```json
{
  "id": "5e9f2147-05a1-4eae-bf50-13f7dfc00221",
  "tutorId": "baddd584-b456-11f0-95be-c4efbbb92864",
  "title": "Sessão com Ana Rita — Design",
  "description": "Sessão agendada pela app Peerly.",
  "startsAt": "2025-11-08T15:00:00",
  "endsAt": "2025-11-08T16:00:00",
  "status": "scheduled",
  "tutor": {
    "id": "baddd584-b456-11f0-95be-c4efbbb92864",
    "name": "Ana Rita",
    "avatarUrl": "https://peerly.app/avatars/rita.jpg"
  }
}
```

#### Error 404
```json
{
  "error": "Session not found"
}
```

---

### POST `/sessions`
Cria uma nova sessão.

#### Request Body
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

#### Response 200
```json
{
  "id": "b0f4c0fa-6f2e-4c8e-b9a5-4f978d923fa2",
  "tutorId": "baddd584-b456-11f0-95be-c4efbbb92864",
  "title": "Sessão de UI/UX com Ana Rita",
  "startsAt": "2025-11-08T09:00:00",
  "endsAt": "2025-11-08T10:00:00",
  "status": "scheduled"
}
```

#### Error Responses
| Código | Mensagem |
|--------|-----------|
| 400 | Campos obrigatórios: tutorId, title, startsAt, endsAt |
| 500 | Internal Server Error (ex.: tutor inexistente) |

---

### DELETE `/sessions/{id}`
Remove uma sessão (caso seja cancelada).

#### Response 204
```
(no content)
```

---

##  Common Status Codes

| Código | Significado |
|--------|--------------|
| 200 | Sucesso |
| 201 | Criado com sucesso |
| 204 | Sem conteúdo (remoção bem-sucedida) |
| 400 | Requisição inválida |
| 401 | Não autorizado |
| 403 | Acesso negado |
| 404 | Não encontrado |
| 500 | Erro interno do servidor |

---

##  Modelo de Dados (resumo)

| Entidade | Campos principais |
|-----------|------------------|
| **User** | id, email, fullName, passwordHash, role, avatarUrl |
| **Session** | id, tutorId, title, description, startsAt, endsAt, status |
| **TutorSlimDto** | id, name, avatarUrl |

