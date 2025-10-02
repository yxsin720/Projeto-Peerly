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
