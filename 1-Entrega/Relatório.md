# Proposta Inicial

### Nome do Projeto
Peerly

### Ideia Central
O Peerly é uma aplicação móvel de aprendizagem colaborativa (peer-to-peer) que conecta estudantes com interesses e competências complementares.  
Através de perfis personalizados, os utilizadores podem encontrar colegas, agendar sessões de estudo e trocar mensagens, criando uma rede académica dinâmica e acessível.

### Motivação
A dificuldade em encontrar colegas com competências específicas limita a colaboração e o desenvolvimento académico.  
O Peerly surge como resposta a esse desafio, promovendo uma comunidade digital que valoriza a partilha de conhecimento e o crescimento conjunto entre estudantes.

---

## Objetivos do Projeto

### Gerais
- Aproximar estudantes com interesses e necessidades comuns.  
- Facilitar a troca de conhecimento de forma intuitiva e segura.  
- Criar uma rede de apoio académico colaborativo.

### Específicos
- Permitir a criação de perfis personalizados com áreas de interesse.  
- Promover o emparelhamento entre estudantes e tutores.  
- Disponibilizar ferramentas de comunicação e agendamento de sessões.

---

## Público-Alvo
Estudantes universitários e do ensino secundário que procuram apoio em áreas específicas de estudo ou desejam partilhar as suas competências com outros colegas.

---

## Pesquisa de Mercado
O Peerly diferencia-se por centrar-se exclusivamente em estudantes e no conceito de troca direta de competências.  
Foram analisadas algumas plataformas com propósitos semelhantes:

- **LinkedIn:** voltado para o mercado profissional.  
- **Discord/Slack:** utilizados em comunidades, mas pouco estruturados por áreas de estudo.  
- **Skillshare:** orientado para cursos formais e não para colaboração direta entre estudantes.

---

## Missão e Visão

**Missão:**  
Facilitar a troca de conhecimento entre estudantes através de uma aplicação peer-to-peer, permitindo que qualquer utilizador possa aprender e ensinar nas suas áreas de interesse.

**Visão:**  
Tornar-se uma plataforma de referência em aprendizagem colaborativa, promovendo partilha, acessibilidade e crescimento académico.

---

## Escopo do Projeto

**Objetivo:**  
Desenvolver uma aplicação móvel que promova a aprendizagem colaborativa entre estudantes, permitindo a criação de perfis, definição de áreas de interesse e emparelhamento entre pares para sessões de estudo.

**Funcionalidades Incluídas:**  
- Registo e autenticação de utilizadores  
- Criação e edição de perfis  
- Emparelhamento entre utilizadores  
- Chat em tempo real  
- Agendamento de sessões  

**Funcionalidades Excluídas:**  
- Sistema de gamificação  
- Integração com redes sociais externas  
- Suporte a outras plataformas além de Android  
- Videochamadas avançadas  

---

## Entregáveis da 1ª Entrega
- Estrutura inicial do repositório GitHub  
- Mockups organizados por ecrã  
- Modelo de domínio visual  
- Gráfico de Gantt  
- Poster de apresentação  
- Documentação em Markdown  

---

## Estrutura de Trabalho (WBS)

| Categoria | Tarefa | Responsável |
|------------|---------|-------------|
| Gestão do Projeto | Organização no ClickUp | Todos |
|  | Casos de utilização | Francisco Baptista |
|  | Estruturação do repositório (GitHub) | Yassin Mac-Arthur |
|  | Relatório final | Yassin Mac-Arthur |
| Design e Documentação | Criação do poster | Francisco Silva |
|  | Planeamento e Gantt | Nayuka Malebo |
|  | Modelo de domínio | Yassin Mac-Arthur |
| Desenvolvimento Técnico | Mockups e UI/UX | Francisco Baptista |

---

## Requisitos Funcionais

1. **Registo e Autenticação**  
   O sistema deve permitir a criação de contas de utilizador, a autenticação segura de credenciais e a recuperação de palavras-passe.

2. **Gestão de Perfil**  
   O utilizador deve poder editar os seus dados pessoais, indicar áreas de interesse e atualizar elementos visuais do perfil (fotografia e descrição).

3. **Emparelhamento de Utilizadores**  
   A aplicação deve disponibilizar um mecanismo de correspondência entre estudantes e tutores, com base nas áreas de interesse previamente definidas.

4. **Agendamento de Sessões**  
   O sistema deve possibilitar a marcação de sessões de tutoria em datas e horários específicos, notificando o tutor selecionado para confirmação.

5. **Comunicação**  
   A aplicação deve suportar comunicação em tempo real através de chat e chamadas de vídeo básicas.

6. **Avaliação e Feedback**  
   Após a conclusão de uma sessão, o utilizador deve poder avaliar a experiência e deixar comentários qualitativos que ficam associados ao perfil do par.

---

## Requisitos Não Funcionais

1. **Usabilidade**  
   A interface deve ser intuitiva e de fácil utilização, garantindo acessibilidade a utilizadores com diferentes níveis de literacia digital.

2. **Desempenho**  
   O sistema deve assegurar tempos de resposta adequados, permitindo a troca de mensagens em tempo real sem atrasos significativos.

3. **Segurança**  
   As credenciais dos utilizadores devem ser armazenadas de forma encriptada e todas as comunicações entre cliente e servidor devem ser seguras.

4. **Compatibilidade**  
   A aplicação deve ser compatível com dispositivos Android recentes e o backend deve disponibilizar serviços através de uma API REST.

5. **Fiabilidade**  
   O sistema deve garantir a persistência dos dados e permitir a recuperação de mensagens ou sessões em caso de falha.

6. **Escalabilidade**  
   A arquitetura deve permitir o crescimento futuro da plataforma, suportando mais utilizadores e novas funcionalidades.

---

## Casos de Utilização

### UC1 – Agendar uma Sessão de Tutoria Peer-to-Peer
**Ator Principal:** Estudante  
**Objetivo:** Encontrar um tutor, combinar um horário e agendar uma sessão.  
**Pré-condições:** O estudante tem conta ativa e está autenticado.  

**Fluxo Principal:**
1. O estudante abre a aplicação e seleciona “Encontrar tutor”.  
2. Pesquisa por disciplina (ex.: Matemática).  
3. Visualiza a lista de tutores disponíveis.  
4. Seleciona um tutor e consulta o perfil.  
5. Agenda sessão para data/hora específica.  
6. O tutor recebe notificação e confirma.  
7. A sessão é registada no sistema.  

---

### UC2 – Participar numa Sessão Agendada
**Atores Principais:** Estudante e Tutor  
**Objetivo:** Realizar a sessão de tutoria previamente combinada.  
**Pré-condições:** Sessão confirmada e registada no sistema.  

**Fluxo Principal:**
1. O utilizador recebe notificação da sessão.  
2. Acede à secção “Sessões”.  
3. Entra na sessão agendada.  
4. A sessão decorre via videochamada ou chat.  
5. No final, é marcada como concluída.

---

### UC3 – Avaliar e Dar Feedback Após Sessão
**Atores Principais:** Estudante e Tutor  
**Objetivo:** Avaliar a qualidade da sessão e do par.  
**Pré-condições:** Sessão concluída.  

**Fluxo Principal:**
1. Após a sessão, a aplicação apresenta o ecrã de feedback.  
2. O utilizador atribui uma classificação (1 a 5 estrelas).  
3. Adiciona comentário opcional.  
4. O feedback é registado e associado ao perfil avaliado.  

---

## Guiões de Teste

### GT1 – Criar Conta
**Objetivo:** Validar o processo de registo de um novo utilizador.  
**Pré-condição:** O utilizador não tem conta criada.  

**Passos:**
1. O utilizador abre a aplicação Peerly.  
2. Seleciona “Criar conta”.  
3. Introduz nome, email e palavra-passe.  
4. Confirma e clica em “Registar”.  
5. O sistema confirma a criação da conta.  

**Resultado Esperado:** Conta criada com sucesso e utilizador autenticado.

---

### GT2 – Agendar Sessão de Tutoria
**Objetivo:** Validar o agendamento de uma sessão peer-to-peer.  
**Pré-condição:** O utilizador tem conta ativa e está autenticado.  

**Passos:**
1. O utilizador abre a aplicação.  
2. Pesquisa por disciplina e escolhe um tutor.  
3. Seleciona data e hora disponíveis.  
4. Confirma o agendamento.  

**Resultado Esperado:** Sessão registada e notificação enviada ao tutor.

---

### GT3 – Dar Feedback Após Sessão
**Objetivo:** Validar o processo de avaliação de uma sessão concluída.  
**Pré-condição:** O utilizador participou numa sessão agendada.  

**Passos:**
1. Após a sessão, é apresentado o ecrã de feedback.  
2. O utilizador atribui uma classificação e comentário.  
3. Submete a avaliação.  

**Resultado Esperado:** Feedback registado e associado ao perfil do tutor/estudante avaliado.

---

## Equipa do Projeto
- Francisco Baptista  
- Francisco Silva  
- Nayuka Malebo  
- Yassin Mac-Arthur

**Docentes:**  
Pedro Rosa  
Nathan Campos  

