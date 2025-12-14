# Modelo de Domínio — Peerly

O modelo de domínio do **Peerly** representa as principais entidades do sistema e as suas relações.

---

## Entidades Principais

### Utilizador
- id (PK)  
- nome  
- email  
- curso  

### Tutor
- id (PK)  
- user_id (FK)  
- bio  
- avaliação_média  

### Disciplina
- id (PK)  
- nome  

### Sessão
- id (PK)  
- tutor_id (FK)  
- estudante_id (FK)  
- disciplina_id (FK)  
- data_hora  
- estado  

### Mensagem
- id (PK)  
- remetente_id (FK)  
- destinatário_id (FK)  
- conteúdo  
- data_envio  

### Avaliação
- id (PK)  
- sessão_id (FK)  
- utilizador_id (FK)  
- rating  
- comentário  

### Notificação
- id (PK)  
- utilizador_id (FK)  
- mensagem  
- data_envio  

---

## Relações Essenciais
- Um **Utilizador** pode ser também um **Tutor**.  
- Um **Tutor** pode lecionar várias **Disciplinas**.  
- Um **Utilizador** pode participar em várias **Sessões**.  
- Cada **Sessão** está ligada a uma **Disciplina** e a um **Tutor**.  
- Um **Utilizador** pode enviar **Mensagens** e fazer **Avaliações**.  
- Cada **Utilizador** pode receber várias **Notificações**.  

---

## Diagrama do modelo de domínio:

<p align="center">
  <img src="https://github.com/user-attachments/assets/08b0fa32-147b-4e3d-be54-c35bbb7bf900" 
       alt="Diagrama_ER_Peerly" 
       width="700">
</p>
>


