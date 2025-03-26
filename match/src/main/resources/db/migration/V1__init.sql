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
    deleted_date     datetime     null,
    constraint UK_domain_event_uuid unique (uuid)
);


create table if not exists kafka_message_consume_history
(
    id           bigint auto_increment primary key,
    uuid         varchar(255) not null,
    topic        varchar(255) not null,
    created_date datetime     not null DEFAULT CURRENT_TIMESTAMP,
    updated_date datetime     null,
    deleted_date datetime     null,
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
    deleted_date datetime     null
);

create table if not exists member
(
    id              bigint auto_increment primary key,
    age             int          not null,
    nickname        varchar(10)  not null,
    username        varchar(50)  not null,
    hashed_password varchar(255) not null,
    gender          varchar(6)   not null,
    created_date    datetime     not null DEFAULT CURRENT_TIMESTAMP,
    updated_date    datetime     null,
    deleted_date    datetime     null,
    constraint UK_member_username unique (username)
);

create table if not exists filter
(
    id                           bigint auto_increment primary key,
    member_id                    bigint      not null,
    gender_condition             varchar(11) not null,
    max_include_age              int         not null,
    max_include_distance_from_me int         not null,
    min_include_age              int         not null,
    permit_excess_age            bool        not null,
    permit_excess_distance       bool        not null,
    created_date                 datetime    not null DEFAULT CURRENT_TIMESTAMP,
    updated_date                 datetime    null,
    deleted_date                 datetime    null,
    constraint UK_filter_member_id unique (member_id)
);

create table if not exists arrow
(
    id           bigint auto_increment primary key,
    sender_id    bigint     not null,
    target_id    bigint     not null,
    like_type    varchar(7) not null,
    created_date datetime   not null DEFAULT CURRENT_TIMESTAMP,
    updated_date datetime   null,
    deleted_date datetime   null,
    constraint UK_arrow_sender_id_target_id unique (sender_id, target_id)
);

create table if not exists couple
(
    id           bigint auto_increment primary key,
    lower_id     bigint   not null,
    higher_id    bigint   not null,
    created_date datetime not null DEFAULT CURRENT_TIMESTAMP,
    updated_date datetime null,
    deleted_date datetime null,
    constraint UK_couple_higher_id_lower_id unique (higher_id, lower_id)
);
