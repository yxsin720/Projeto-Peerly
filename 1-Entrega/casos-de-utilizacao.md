# Casos de Utilização — Peerly

---

## UC1 – Agendar uma sessão de tutoria peer-to-peer (Core)

- **Ator principal:** Estudante (utilizador)  
- **Objetivo:** Encontrar um tutor, combinar um horário e agendar sessão  
- **Pré-condições:** O estudante tem conta ativa e está autenticado  

**Fluxo principal:**
1. O estudante abre a aplicação e seleciona “Encontrar tutor”.  
2. Pesquisa por disciplina (ex.: Matemática).  
3. Visualiza a lista de tutores disponíveis.  
4. Seleciona um tutor e consulta o perfil e disponibilidade.  
5. Agenda sessão para data/hora específica.  
6. O tutor recebe notificação e confirma.  
7. A sessão aparece na agenda de ambos.  

- **Pós-condições:** Sessão registada no sistema e visível na agenda de estudante e tutor.  
- **Observação:** Este é o caso de utilização central da Peerly, pois representa a função principal de conectar pares para aprender/ensinar.  

---

## UC2 – Participar numa sessão agendada

- **Atores principais:** Estudante / Tutor  
- **Objetivo:** Realizar a sessão de tutoria previamente combinada  
- **Pré-condições:** Sessão confirmada e registada na agenda  

**Fluxo principal:**
1. O utilizador recebe notificação de que a sessão vai começar.  
2. Abre a aplicação e acede à secção “Sessões”.  
3. Entra na sessão agendada.  
4. A sessão decorre (via videochamada, chat ou presencial, dependendo da versão).  
5. No final, a aplicação regista a sessão como concluída.  

- **Pós-condições:** Sessão marcada como concluída no sistema.  

---

## UC3 – Avaliar e dar feedback após sessão

- **Atores principais:** Estudante e Tutor  
- **Objetivo:** Avaliar a qualidade da sessão e do par (tutor/estudante)  
- **Pré-condições:** Sessão concluída  

**Fluxo principal:**
1. Após a sessão, a aplicação apresenta o ecrã de feedback.  
2. O utilizador atribui uma classificação (1 a 5 estrelas).  
3. O utilizador pode adicionar um comentário opcional.  
4. O feedback fica associado ao perfil do tutor/estudante.  
5. O sistema atualiza a reputação e melhora as sugestões futuras.  

- **Pós-condições:** Feedback registado e associado ao perfil do utilizador avaliado.  
