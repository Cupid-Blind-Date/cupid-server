create table if not exists domain_event
(
    id               bigint auto_increment primary key,
    target_domain_id bigint       not null,
    event_type       varchar(31)  not null,
    fail_reason      varchar(255) null,
    uuid             varchar(255) null,
    state            varchar(255) null,
    created_date     datetime     not null DEFAULT CURRENT_TIMESTAMP,
    updated_date     datetime     null,
    deleted_at       datetime     null,
    constraint UK_domain_event_uuid unique (uuid)
);

create table if not exists kafka_message_consume_history
(
    id           bigint auto_increment primary key,
    uuid         varchar(255) not null,
    topic        varchar(255) not null,
    created_date datetime     not null DEFAULT CURRENT_TIMESTAMP,
    updated_date datetime     null,
    deleted_at   datetime     null,
    constraint UK_kafka_message_consume_history_uuid unique (uuid)
);

create table if not exists dead_letter
(
    id           bigint auto_increment primary key,
    uuid         varchar(255) not null,
    recovered    bool         not null,
    created_date datetime     not null DEFAULT CURRENT_TIMESTAMP,
    fail_reason  varchar(255) null,
    updated_date datetime     null,
    deleted_at   datetime     null
);

create table chat_room
(
    id           bigint auto_increment primary key,
    higher_id    bigint   not null,
    lower_id     bigint   not null,
    created_date datetime not null DEFAULT CURRENT_TIMESTAMP,
    updated_date datetime null,
    deleted_at   datetime null,
    constraint UK_chat_room_higher_id_lower_id unique (higher_id, lower_id)
);
create table if not exists domain_event
(
    id               bigint auto_increment primary key,
    target_domain_id bigint       not null,
    event_type       varchar(31)  not null,
    fail_reason      varchar(255) null,
    uuid             varchar(255) null,
    state            varchar(255) null,
    created_date     datetime     not null DEFAULT CURRENT_TIMESTAMP,
    updated_date     datetime     null,
    deleted_at       datetime     null,
    constraint UK_domain_event_uuid unique (uuid)
);

create table if not exists kafka_message_consume_history
(
    id           bigint auto_increment primary key,
    uuid         varchar(255) not null,
    topic        varchar(255) not null,
    created_date datetime     not null DEFAULT CURRENT_TIMESTAMP,
    updated_date datetime     null,
    deleted_at   datetime     null,
    constraint UK_kafka_message_consume_history_uuid unique (uuid)
);

create table if not exists dead_letter
(
    id           bigint auto_increment primary key,
    uuid         varchar(255) not null,
    recovered    bool         not null,
    created_date datetime     not null DEFAULT CURRENT_TIMESTAMP,
    fail_reason  varchar(255) null,
    updated_date datetime     null,
    deleted_at   datetime     null
);

create table chat_room
(
    id           bigint auto_increment primary key,
    higher_id    bigint   not null,
    lower_id     bigint   not null,
    created_date datetime not null DEFAULT CURRENT_TIMESTAMP,
    updated_date datetime null,
    deleted_at   datetime null,
    constraint UK_chat_room_higher_id_lower_id unique (higher_id, lower_id)
);

create table if not exists chat_message
(
    id                bigint auto_increment primary key,
    chat_room_id      bigint       not null,
    sender_id         bigint       not null,
    target_id         bigint       not null,
    message           varchar(255) not null,
    chat_message_type varchar(30)  not null,
    `read`            bool         not null DEFAULT false,
    created_date      datetime     not null DEFAULT CURRENT_TIMESTAMP,
    updated_date      datetime     null,
    deleted_at        datetime     null
);

create index IDX_chat_message_chat_room_id_target_id
    on chat_message (chat_room_id, target_id);
