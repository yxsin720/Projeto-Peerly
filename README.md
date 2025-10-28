# Proposta Inicial

## Nome do Projeto
Peerly

## Ideia Central
O Peerly é uma aplicação móvel de aprendizagem colaborativa (peer-to-peer) que conecta estudantes com interesses e competências complementares.  
Através de perfis personalizados, os utilizadores podem encontrar colegas, agendar sessões de estudo, trocar mensagens e realizar videochamadas, criando uma rede académica dinâmica e acessível.

## Motivação
A dificuldade em encontrar colegas com competências específicas limita a colaboração e o desenvolvimento académico.  
O Peerly surge como resposta a este desafio, promovendo uma comunidade digital que valoriza a partilha de conhecimento, incentiva a cooperação e fortalece o crescimento pessoal e coletivo.

## Objetivos Gerais
- Aproximar estudantes com interesses e necessidades comuns.  
- Facilitar a troca de conhecimento de forma rápida, intuitiva e segura.  
- Disponibilizar ferramentas de comunicação integradas (chat e vídeo).  
- Contribuir para o desenvolvimento de competências individuais e para a criação de uma rede de apoio académico.

## Enquadramento do Projeto

O **Pearly** é uma aplicação móvel que promove a ajuda entre estudantes através da criação de perfis pessoais, onde cada utilizador indica as suas capacidades, interesses e áreas de estudo.  
Outros estudantes podem aceder a estes perfis para trocar conhecimentos, enviar mensagens e realizar chamadas de vídeo.

O problema identificado é a dificuldade em encontrar colegas com competências complementares, dentro ou fora do contexto académico, o que limita oportunidades de colaboração, estudo conjunto e desenvolvimento de novas capacidades.

---

### Objetivos e Motivação

A motivação principal reside na importância da cooperação académica e no potencial de criar uma comunidade digital de aprendizagem colaborativa.  
Os objetivos do projeto são:

- Criar uma plataforma intuitiva que aproxime estudantes com interesses comuns.  
- Incentivar a troca de conhecimento de forma acessível, rápida e segura.  
- Oferecer ferramentas de comunicação integradas (chat e vídeo).  
- Contribuir para a valorização das competências individuais de cada estudante.  

---

### Público-Alvo

O público-alvo principal são **estudantes universitários e do ensino secundário** que procuram apoio em áreas específicas de estudo ou que desejam partilhar e praticar as suas competências com colegas.

---

### Pesquisa de Mercado

O **Pearly** diferencia-se ao centrar-se exclusivamente em estudantes e no conceito de troca de competências direta (peer-to-peer).  
Foram analisadas algumas plataformas de networking académico e profissional, como:

- **LinkedIn** → Focado em networking profissional, mas pouco direcionado para estudantes em fase de aprendizagem.  
- **Discord/Slack** → Utilizados em comunidades, mas pouco organizados em torno de perfis de competências individuais.  
- **Skillshare** → Orientado para cursos estruturados, não para colaboração direta entre estudantes.

# Project Charter

## 1. Missão
Facilitar a troca de conhecimento entre pessoas através de uma aplicação mobile peer‑to‑peer, permitindo que qualquer utilizador possa aprender e ensinar em áreas como programação, design e matemática, com recurso a chat e videochamadas.

## 2. Visão
Tornar‑se a plataforma de referência para aprendizagem colaborativa, onde qualquer pessoa pode partilhar competências e adquirir novos conhecimentos de forma simples, acessível e interativa.

---

# Escopo do Projeto

## 3. Objetivo
Desenvolver uma aplicação mobile que facilite a aprendizagem colaborativa entre estudantes, permitindo a criação de perfis, definição de áreas de interesse e emparelhamento entre pares para sessões de estudo.

## 4. Funcionalidades Incluídas (In Scope)
- Registo e autenticação de utilizadores  
- Criação e edição de perfil com áreas de interesse  
- Emparelhamento entre utilizadores com base em interesses comuns  
- Comunicação via chat em tempo real  
- Funcionalidade básica de videochamada  
- Gestão de sessões de aprendizagem (agendamento e estado)  
- Backend com API REST e base de dados relacional  

## 5. Funcionalidades Excluídas (Out of Scope)
- Sistema de gamificação (badges, pontos, rankings)  
- Integração com redes sociais externas  
- Suporte a múltiplas plataformas (apenas Android será desenvolvido)  
- Funcionalidades avançadas de videochamada (gravação, partilha de ecrã)  

---

# Entregáveis da 1ª Entrega
- Estrutura inicial do repositório GitHub  
- Mockups organizados por ecrã  
- Modelo de domínio visual  
- Gráfico de Gantt  
- Poster de apresentação  
- Documentação em Markdown  

---

# Objetivos da Peerly

### Funcionais
- Criar perfis com áreas de interesse  
- Emparelhar utilizadores com interesses comuns  
- Comunicar por chat e realizar videochamadas  

### Técnicos
- App Android em Kotlin/Jetpack Compose  
- Backend em Spring Boot com base de dados relacional  
- Comunicação via API REST documentada  

### De processo
- Organização em ClickUp  
- Versionamento e documentação no GitHub   

---

# Stakeholders

### Equipa do Projeto
- Francisco Baptista
- Francisco Silva
- Nayuka Malebo
- Yassin Mac-Arthur

### Docentes Envolvidos
- Pedro Rosa  
- Nathan Campos  

### Utilizadores Finais
- Estudantes universitários e do ensino secundário  

---

# Critérios de Sucesso (1ª Entrega)
- Repositório organizado com pastas e ficheiros conforme requisitos  
- Mockups completos e integrados no relatório  
- Modelo de domínio visual entregue e referenciado  
- Gráfico de Gantt incluído e contextualizado  
- Poster criado e disponível  
- Documentação clara e em formato Markdown  

---

# Work Breakdown Structure (WBS)

## 1. Gestão do Projeto
- Organização do trabalho em ClickUp — Todos  
- Casos de utilização — Francisco Baptista  
- Estruturação do repositório (GitHub) — Yassin Mac‑Arthur  
- Relatório final — Yassin Mac‑Arthur  

## 2. Design e Documentação
- Criação do poster — Francisco Silva  
- Planeamento e Gantt — Nayuka Malebo  
- Modelo de domínio — Yassin Mac‑Arthur  

## 3. Desenvolvimento Técnico
- Mockups e interfaces (UI/UX) — Francisco Baptista

## Requisitos Funcionais

Os requisitos funcionais definem as operações essenciais que a aplicação deverá disponibilizar aos seus utilizadores:

1. **Registo e Autenticação**  
   O sistema deve permitir a criação de contas de utilizador, a autenticação segura de credenciais e a recuperação de palavras‑passe.

2. **Gestão de Perfil**  
   O utilizador deve poder editar os seus dados pessoais, indicar áreas de interesse e atualizar elementos visuais do perfil (fotografia e descrição).

3. **Emparelhamento de Utilizadores**  
   A aplicação deve disponibilizar um mecanismo de correspondência entre estudantes e tutores, com base nas áreas de interesse previamente definidos.

4. **Agendamento de Sessões**  
   O sistema deve possibilitar a marcação de sessões de tutoria em datas e horários específicos, notificando o tutor selecionado para confirmação.

5. **Comunicação**  
   A aplicação deve suportar comunicação síncrona através de mensagens em tempo real (chat) e de chamadas de vídeo em formato básico.

6. **Avaliação e Feedback**  
   Após a conclusão de uma sessão, o utilizador deve poder avaliar a experiência (através de um sistema de classificação) e fornecer comentários qualitativos, que ficarão associados ao perfil do par.

---

## Requisitos Não Funcionais

Os requisitos não funcionais estabelecem as propriedades de qualidade e as restrições técnicas que o sistema deverá cumprir:

1. **Usabilidade**  
   A interface deve ser intuitiva, consistente e de fácil utilização, garantindo acessibilidade a utilizadores com diferentes níveis de literacia digital.

2. **Desempenho**  
   O sistema deve assegurar tempos de resposta adequados, permitindo a troca de mensagens em tempo real com latência mínima e suportando múltiplos utilizadores em simultâneo sem degradação significativa do serviço.

3. **Segurança**  
   As credenciais dos utilizadores devem ser armazenadas de forma encriptada (hashing de palavras‑passe) e todas as comunicações entre cliente e servidor devem ocorrer através de protocolos seguros (HTTPS).

4. **Compatibilidade**  
   A aplicação deverá ser compatível com dispositivos Android recentes (versão mínima a definir pela equipa) e o backend deverá disponibilizar serviços através de uma API REST devidamente documentada.

5. **Fiabilidade**  
   O sistema deve garantir a persistência dos dados relativos a sessões agendadas. Em caso de falha de rede, deve permitir a recuperação da sessão ou o reenvio de mensagens não entregues.

6. **Escalabilidade**  
   A arquitetura da solução deve ser concebida de forma a permitir a evolução futura da plataforma, suportando um número crescente de utilizadores e a integração de novas funcionalidades sem necessidade de reestruturação completa.

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


# Guiões de Teste

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
