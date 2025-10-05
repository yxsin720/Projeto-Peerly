# Modelo de Domínio — Peerly

O modelo de domínio do Peerly representa as principais entidades do sistema e as suas relações.

---

## Entidades Principais

### Utilizador
- id (PK)
- nome
- email
- curso

### Tutor (especialização de Utilizador)
- especialização
- avaliaçãoMedia

### Sessão
- id (PK)
- data
- hora
- tutor_id (FK)
- estudante_id (FK)
- disciplina_id (FK)

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
- utilizador_id (FK)

---

## 🔗 Relações Essenciais
- Um **Tutor** é um tipo de **Utilizador** (herança).  
- Um **Utilizador** pode agendar várias **Sessões**.  
- Cada **Sessão** está associada a uma única **Disciplina**.  
- Uma **Sessão** pode gerar vários **Feedbacks**.  
- Um **Utilizador** pode receber várias **Notificações**.  

---

## Diagrama

<p align="center">
  <img src="../docs/diagrams/MODELODEDOMINIO.png" alt="MODELO DE DOMINIO" width="550"/>



