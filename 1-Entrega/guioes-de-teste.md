# Guiões de Teste — Peerly

---

## GT1 – Criar conta na aplicação
**Objetivo:** Validar o processo de registo de um novo utilizador.  
**Pré-condições:** O utilizador não tem conta criada.  

**Passos:**
1. O utilizador abre a aplicação Peerly.  
2. Seleciona a opção “Criar conta”.  
3. Introduz nome completo, email e palavra-passe.  
4. Confirma os dados e clica em “Registar”.  
5. O sistema apresenta mensagem de sucesso e redireciona para o ecrã inicial.  

**Resultado esperado:** Conta criada com sucesso e utilizador autenticado.

---

## GT2 – Agendar sessão de tutoria
**Objetivo:** Validar o agendamento de uma sessão peer-to-peer.  
**Pré-condições:** O utilizador tem conta ativa e está autenticado.  

**Passos:**
1. O utilizador abre a aplicação e seleciona “Encontrar tutor”.  
2. Pesquisa por disciplina (ex.: Matemática).  
3. Escolhe um tutor da lista e consulta o perfil.  
4. Seleciona data e hora disponíveis.  
5. Confirma o agendamento.  

**Resultado esperado:** Sessão registada na agenda do utilizador e notificação enviada ao tutor.

---

## GT3 – Dar feedback após sessão
**Objetivo:** Validar o processo de avaliação de uma sessão concluída.  
**Pré-condições:** O utilizador participou numa sessão agendada.  

**Passos:**
1. Após a sessão, a aplicação apresenta o ecrã de feedback.  
2. O utilizador atribui uma classificação (1 a 5 estrelas).  
3. Opcionalmente, escreve um comentário.  
4. Submete o feedback.  

**Resultado esperado:** Feedback registado e associado ao perfil do tutor/estudante avaliado.

