# 📘 Peerly — Guia de Dados (Versão Atualizada)

## 1. Visão Geral
A **Peerly** é uma plataforma de tutoria digital que liga **tutores** e **alunos** através de sessões agendadas, com chat em tempo real e integração com **Google Meet**.

---

## 2. Entidades Principais

### 2.1 User
Representa qualquer utilizador do sistema (tutor ou aluno).

| Campo | Tipo | Descrição |
|-----|-----|----------|
| id | UUID | Identificador único |
| email | String | Email único |
| fullName | String | Nome completo |
| passwordHash | String | Palavra-passe encriptada |
| role | Enum (`student`, `tutor`, `both`) | Papel do utilizador |
| avatarUrl | String | URL do avatar |
| language | String | Idioma |
| createdAt | LocalDateTime | Data de criação |

**Regras**
- Email único  
- Password nunca devolvida pela API  

---

### 2.2 Session
Representa uma sessão de tutoria.

| Campo | Tipo | Descrição |
|-----|-----|----------|
| id | UUID | Identificador |
| tutorId | UUID | Tutor |
| studentId | UUID | Aluno |
| title | String | Título |
| description | String | Descrição |
| startsAt | LocalDateTime | Início |
| endsAt | LocalDateTime | Fim |
| status | Enum | Estado |
| meetUrl | String | Link Meet |

**Estados**
- scheduled
- completed
- cancelled

---

### 2.3 TutorSlimDto
DTO usado em respostas da API.

| Campo | Tipo |
|-----|-----|
| id | UUID |
| name | String |
| avatarUrl | String |

---

## 3. Relações
- Um tutor pode ter várias sessões  
- Um aluno pode participar em várias sessões  
- Cada sessão tem exatamente um tutor  

---

## 4. Integração Google Meet
- Criação automática via Google Calendar API  
- Link armazenado em `Session.meetUrl`  
- Tutor e aluno adicionados como participantes  

---

## 5. Segurança
- Passwords com hash  
- Dados sensíveis nunca expostos  

---

## 6. Evoluções Futuras
- Avaliações  
- Pagamentos  
- Sessões em grupo  

---

## 7. Resumo
Documento alinhado com a **API REST Peerly**, backend Spring Boot e app Android.
