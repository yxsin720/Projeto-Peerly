# 📘 Peerly REST API

Documentação oficial da **Peerly REST API**, responsável pela autenticação, gestão de utilizadores, sessões de tutoria e integração com **Google Meet**.

---

## 🌐 Base URL (Desenvolvimento Local)

```
http://10.0.2.2:8080/api
```

> Nota: `10.0.2.2` permite que o Android Emulator aceda ao backend local.

---

## 📦 Formato de Dados

- Todas as rotas utilizam **JSON**
- Header obrigatório:

```
Content-Type: application/json
```

---

## 🔐 AUTHENTICATION

### POST `/auth/login`

Efetua o login de um utilizador existente.

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

---

## 👤 USERS

### GET `/users`
Lista todos os utilizadores registados.

### POST `/users`
Cria um novo utilizador.

---

## 🎓 TUTORS

Os tutores são utilizadores com:
```
role = "tutor"
```

---

## 📅 SESSIONS

### GET `/sessions`
Lista todas as sessões de tutoria.

### GET `/sessions/{id}`
Obtém detalhes de uma sessão.

### POST `/sessions`
Cria uma nova sessão.

### DELETE `/sessions/{id}`
Remove uma sessão.

---

## 🎥 GOOGLE MEET

- Geração automática de links Google Meet
- Integração via Google Calendar API
- Partilha automática do link no chat

---

## 📊 HTTP STATUS

| Código | Significado |
|------|------------|
| 200 | Sucesso |
| 201 | Criado |
| 204 | Sem conteúdo |
| 400 | Pedido inválido |
| 401 | Não autorizado |
| 404 | Não encontrado |
| 500 | Erro interno |
