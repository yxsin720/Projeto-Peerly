# Peerly — Dicionário de Dados (v1)

Este documento descreve as tabelas, colunas, tipos e regras do modelo de dados da aplicação **Peerly**.

> Notação: *NN* = NOT NULL; *PK* = Primary Key; *FK* = Foreign Key; *UQ* = Unique; *DEF* = Default

## Tabela: `users`
| Coluna        | Tipo           | Regras / DEF                                   | Descrição |
|---|---|---|---|
| id            | VARCHAR(36)    | PK                                             | UUID do utilizador |
| email         | VARCHAR(254)   | NN, UQ                                         | Email de login |
| password_hash | TEXT           |                                                | Hash da palavra‑passe (ou `NULL` se OAuth) |
| full_name     | VARCHAR(200)   | NN                                             | Nome completo |
| role          | ENUM(...)      | NN, DEF='student'                              | Perfil: student/tutor/both/admin |
| avatar_url    | TEXT           |                                                | URL do avatar |
| language      | VARCHAR(10)    | NN, DEF='pt'                                   | Idioma preferido |
| created_at    | TIMESTAMP      | NN, DEF=CURRENT_TIMESTAMP                      | Data de criação |
| updated_at    | TIMESTAMP      | NN, DEF=CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Última atualização |

## Tabela: `oauth_accounts`
| Coluna        | Tipo         | Regras / DEF | Descrição |
|---|---|---|---|
| id            | VARCHAR(36)  | PK           | UUID da ligação OAuth |
| user_id       | VARCHAR(36)  | NN, FK→users(id) ON DELETE CASCADE | Dono da conta OAuth |
| provider      | VARCHAR(40)  | NN           | Ex.: google, apple |
| provider_uid  | VARCHAR(191) | NN, UQ (provider, provider_uid) | ID externo do provedor |
| created_at    | TIMESTAMP    | NN, DEF=CURRENT_TIMESTAMP | Data de ligação |

## Tabela: `subjects`
| Coluna | Tipo | Regras / DEF | Descrição |
|---|---|---|---|
| id   | VARCHAR(36) | PK | UUID |
| slug | VARCHAR(120) | NN, UQ | Identificador curto |
| name | VARCHAR(120) | NN | Nome legível |

## Tabela: `tutor_subjects`
| Coluna | Tipo | Regras / DEF | Descrição |
|---|---|---|---|
| tutor_id | VARCHAR(36) | PK, FK→users(id) ON DELETE CASCADE | Tutor |
| subject_id | VARCHAR(36) | PK, FK→subjects(id) ON DELETE CASCADE | Disciplina |
| level | VARCHAR(40) |  | Nível ofertado |
| price_per_hour_cents | INT |  | Preço por hora (centavos) |

## Tabela: `tutor_availability`
| Coluna | Tipo | Regras / DEF | Descrição |
|---|---|---|---|
| id | VARCHAR(36) | PK | UUID |
| tutor_id | VARCHAR(36) | NN, FK→users(id) ON DELETE CASCADE | Tutor |
| weekday | TINYINT | NN, CHECK 0..6 | Dia da semana (0=Domingo) |
| start_time | TIME | NN | Hora de início |
| end_time | TIME | NN, CHECK start<end | Hora de fim |
| timezone | VARCHAR(64) | NN, DEF='UTC' | Fuso horário |

## Tabela: `tutor_time_off`
| Coluna | Tipo | Regras / DEF | Descrição |
|---|---|---|---|
| id | VARCHAR(36) | PK | UUID |
| tutor_id | VARCHAR(36) | NN, FK→users(id) ON DELETE CASCADE | Tutor |
| start_at | DATETIME | NN, CHECK start<end | Início (UTC) |
| end_at | DATETIME | NN | Fim (UTC) |
| reason | VARCHAR(255) |  | Motivo |

## Tabela: `sessions`
| Coluna | Tipo | Regras / DEF | Descrição |
|---|---|---|---|
| id | VARCHAR(36) | PK | UUID |
| tutor_id | VARCHAR(36) | NN, FK→users(id) ON DELETE RESTRICT | Tutor |
| subject_id | VARCHAR(36) | FK→subjects(id) ON DELETE SET NULL | Disciplina |
| title | VARCHAR(200) | NN | Título |
| description | TEXT |  | Descrição |
| starts_at | DATETIME | NN, CHECK start<end | Início (UTC) |
| ends_at | DATETIME | NN | Fim (UTC) |
| status | ENUM(...) | NN, DEF='scheduled' | Estado |
| max_participants | SMALLINT | NN, DEF=1 | Capacidade |
| price_total_cents | INT |  | Preço total |
| created_at | TIMESTAMP | NN, DEF=CURRENT_TIMESTAMP | Criação |

## Tabela: `session_participants`
| Coluna | Tipo | Regras / DEF | Descrição |
|---|---|---|---|
| session_id | VARCHAR(36) | PK, FK→sessions(id) ON DELETE CASCADE | Sessão |
| user_id | VARCHAR(36) | PK, FK→users(id) ON DELETE CASCADE | Participante |
| joined_at | TIMESTAMP | NN, DEF=CURRENT_TIMESTAMP | Data de adesão |

## Tabela: `messages`
| Coluna | Tipo | Regras / DEF | Descrição |
|---|---|---|---|
| id | VARCHAR(36) | PK | UUID |
| session_id | VARCHAR(36) | NN, FK→sessions(id) ON DELETE CASCADE | Sessão |
| sender_id | VARCHAR(36) | FK→users(id) ON DELETE SET NULL | Remetente |
| type | ENUM(text,system,file) | NN, DEF='text' | Tipo de mensagem |
| content | MEDIUMTEXT |  | Conteúdo |
| created_at | TIMESTAMP | NN, DEF=CURRENT_TIMESTAMP | Data |

## Tabela: `reviews`
| Coluna | Tipo | Regras / DEF | Descrição |
|---|---|---|---|
| id | VARCHAR(36) | PK | UUID |
| session_id | VARCHAR(36) | NN, FK→sessions(id) ON DELETE CASCADE | Sessão |
| reviewer_id | VARCHAR(36) | NN, FK→users(id) ON DELETE CASCADE | Aluno avaliador |
| tutor_id | VARCHAR(36) | NN, FK→users(id) ON DELETE CASCADE | Tutor avaliado |
| rating | TINYINT | NN, CHECK 1..5 | Pontuação |
| comment | TEXT |  | Comentário |
| created_at | TIMESTAMP | NN, DEF=CURRENT_TIMESTAMP | Data |

## Tabela: `favorites`
| Coluna | Tipo | Regras / DEF | Descrição |
|---|---|---|---|
| user_id | VARCHAR(36) | PK, FK→users(id) ON DELETE CASCADE | Dono |
| tutor_id | VARCHAR(36) | PK, FK→users(id) ON DELETE CASCADE | Tutor favorito |
| created_at | TIMESTAMP | NN, DEF=CURRENT_TIMESTAMP | Data |

## Tabela: `notifications`
| Coluna | Tipo | Regras / DEF | Descrição |
|---|---|---|---|
| id | VARCHAR(36) | PK | UUID |
| user_id | VARCHAR(36) | NN, FK→users(id) ON DELETE CASCADE | Destinatário |
| title | VARCHAR(200) | NN | Título |
| body | TEXT |  | Corpo |
| is_read | TINYINT(1) | NN, DEF=0 | Lida? |
| created_at | TIMESTAMP | NN, DEF=CURRENT_TIMESTAMP | Data |

## View: `tutor_rating`
| Coluna | Tipo | Descrição |
|---|---|---|
| tutor_id | VARCHAR(36) | ID do tutor |
| avg_rating | DECIMAL(10,2) | Média de avaliações |
| reviews_count | BIGINT | Nº de reviews |

## Regras e Notas
- **Chaves primárias**: UUID gerados via *triggers* `trg_*_uuid` quando não fornecidos.
- **Fusos horários**: datas/hours guardadas em UTC; `tutor_availability.timezone` indica o fuso do tutor.
- **Integridade**: *CHECKs* básicos para *weekday*, janelas de tempo e *rating*.
- **Soft constraints**: evitar overlaps de sessões e *time off* deve ser validado na aplicação/serviços.
