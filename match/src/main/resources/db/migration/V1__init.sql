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
    id                    bigint auto_increment primary key,
    age                   int            not null,
    nickname              varchar(10)    not null,
    username              varchar(50)    not null,
    hashed_password       varchar(255)   not null,
    targetGender          varchar(6)     not null,
    -- 가장 최근 활동일시
    last_active_date      datetime       null,
    -- 가장 최근 접속한 지역의 위도 (-90.00000000 ~ +90.00000000, 소수점 아래 8자리)
    last_active_latitude  DECIMAL(10, 8) null,
    -- 가장 최근 접속한 지역의 경도 (-180.00000000 ~ +180.00000000, 소수점 아래 8자리)
    last_active_longitude DECIMAL(11, 8) null,
    created_date          datetime       not null DEFAULT CURRENT_TIMESTAMP,
    updated_date          datetime       null,
    deleted_date          datetime       null,
    constraint UK_member_username unique (username)
);

CREATE INDEX idx_member_last_active_date ON member (last_active_date);
CREATE INDEX idx_member_age ON member (age);
CREATE INDEX idx_member_gender_lat_lng_last_active_date ON member (targetGender, last_active_latitude,
                                                                   last_active_longitude, last_active_date);
CREATE INDEX idx_member_gender_last_active_date ON member (targetGender, last_active_date);

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
