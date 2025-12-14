# Peerly — BD Report (Resumo)

## 1. Objetivo
A base de dados suporta a plataforma **Peerly** (tutoria e sessões ao vivo). O modelo guarda utilizadores, disciplinas, oferta de tutores, disponibilidade, sessões, participantes, mensagens, avaliações, favoritos e notificações.

## 2. Modelo (alto nível)
- **users**: perfis (student/tutor/both/admin).
- **subjects**: catálogo de disciplinas.
- **tutor_subjects**: relação N:N entre tutores e disciplinas com preço e nível.
- **tutor_availability** / **tutor_time_off**: janelas de disponibilidade / ausências.
- **sessions**: sessões criadas por tutores (com estado e preço).
- **session_participants**: inscrição de alunos em sessões (N:N).
- **messages**: chat por sessão.
- **reviews**: avaliação de sessões/tutores (1 a 5).
- **favorites**: tutores favoritos por aluno.
- **notifications**: alertas para eventos relevantes.
- **tutor_rating (view)**: média e contagem de avaliações por tutor.

## 3. Decisões de desenho
- **UUIDs** como PK para evitar colisões entre serviços e facilitar import/export.
- **UTC** em `DATETIME/TIMESTAMP` para consistência; conversões feitas na app.
- **ENUMs** limitam estados válidos (papéis e estados de sessão).
- **Índices** em campos críticos para consultas comuns (tempo, estado, email, slug).

## 4. Segurança e privacidade
- Passwords guardadas como `password_hash` (hash forte recomendado: Argon2/BCrypt).
- Dados sensíveis mínimos; mensagens podem conter ficheiros (armazenamento externo).

## 5. Dados de exemplo
Incluídos em `populate.sql`: 3 utilizadores, 3 disciplinas, oferta de tutores, uma sessão futura (“Design com Rita”) e participação do aluno Francisco.

## 6. Consultas ilustrativas
Ver `queries.sql` para 15 exemplos: agenda, pesquisa de tutores por preço/assunto, métricas de utilização, mensagens recentes, favoritos, notificações e distribuição por estados.

## 7. Próximos passos
- Procedures para verificar *overlap* de horários e *time off* ao criar sessões.
- Auditoria (tabelas `_audit`) e *soft delete* se necessário.
- Pagamentos: entidades `payments`, `transactions`, *webhooks*.
