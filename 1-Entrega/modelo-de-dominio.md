# Modelo de Domínio — Peerly

O modelo de domínio do Peerly representa as principais entidades do sistema e as suas relações.

---

## Entidades Principais

### Utilizador
- id (PK)
- nome
- email
- curso

### Tutor _(especialização de Utilizador)_
- especialização
- avaliaçãoMedia

### Sessão
- id (PK)
- data
- hora
- estado

### Disciplina
- id (PK)
- nome

### Feedback
- id (PK)
- rating
- comentário

### Notificacao
- id (PK)
- mensagem
- dataEnvio

---

## 🔗 Relações Essenciais
- Um **Tutor** é um tipo de **Utilizador** (herança).  
- Um **Utilizador** pode agendar várias **Sessões**.  
- Cada **Sessão** está associada a uma única **Disciplina**.  
- Uma **Sessão** pode gerar vários **Feedbacks**.  
- Um **Utilizador** pode receber várias **Notificações**.  

---

## Diagrama

<img src="../docs/diagrams/MODELODEDOMINIO.png" alt="MODELO DE DOMINIO" width="550"/>



