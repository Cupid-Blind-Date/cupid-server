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
    id                  bigint auto_increment primary key,
    age                 int            not null,
    nickname            varchar(10)    not null,
    username            varchar(50)    not null,
    hashed_password     varchar(255)   not null,
    gender              varchar(6)     not null,
    -- 가장 최근 활동일시
    last_active_date    datetime       null,
    -- 가장 최근 접속한 지역의 위도 (-90.00000000 ~ +90.00000000, 소수점 아래 8자리)
    latitude            DECIMAL(10, 8) null,
    -- 가장 최근 접속한 지역의 경도 (-180.00000000 ~ +180.00000000, 소수점 아래 8자리)
    longitude           DECIMAL(11, 8) null,
    original_image_name varchar(255)   not null,
    blurred_image_name  varchar(255)   not null,
    created_date        datetime       not null DEFAULT CURRENT_TIMESTAMP,
    updated_date        datetime       null,
    deleted_date        datetime       null,
    constraint UK_member_username unique (username),
    constraint UK_member_original_image_name unique (original_image_name),
    constraint UK_member_blurred_image_name unique (blurred_image_name)
);

-- 거리 조건 없이 쿼리하는 경우
CREATE INDEX idx_member_gender_age_last_active_date ON member (age, gender, last_active_date);
CREATE INDEX idx_member_gender_last_active_date_age ON member (gender, last_active_date, age);

-- 거리 조건과 함께 쿼리하는 경우
CREATE INDEX idx_member_gender_lat_lng_last_active_date_age ON member (gender, latitude, longitude, last_active_date, age);
CREATE INDEX idx_member_age_gender_lat_lng_last_active_date ON member (age, gender, latitude, longitude, last_active_date);

create table if not exists profile_image
(
    id                  bigint auto_increment primary key,
    member_id           bigint       not null,
    original_image_name varchar(255) not null,
    blurred_image_name  varchar(255) not null,
    created_date        timestamp(6),
    deleted_date        timestamp(6),
    updated_date        timestamp(6),
    constraint UK_profile_image_original_image_name unique (original_image_name),
    constraint UK_profile_image_blurred_image_name unique (blurred_image_name)
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
