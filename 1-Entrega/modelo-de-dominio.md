# Modelo de Domínio — Peerly

## Entidades Principais

### Utilizador
- id (PK)
- nome
- email
- password

### Perfil
- id (PK)
- bio
- foto
- utilizador_id (FK)

### ÁreaDeInteresse
- id (PK)
- nome
- perfil_id (FK)

### Sessão
- id (PK)
- utilizador1_id (FK)
- utilizador2_id (FK)
- data_hora
- estado

### Videochamada
- id (PK)
- sessao_id (FK)
- link
- duracao

### Mensagem
- id (PK)
- conteud0
- timestamp
- remetente_id (FK)
- destinatario_id (FK)

## Relações Essenciais
- Um **Utilizador** tem um **Perfil**  
- Um **Perfil** pode ter várias **ÁreasDeInteresse**  
- Um **Utilizador** envia e recebe **Mensagens**  
- Uma **Sessão** envolve dois **Utilizadores**  
- Uma **Videochamada** pertence a uma **Sessão**
