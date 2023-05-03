create table if not exists users (
     id bigint generated by default as identity not null primary key,
     email varchar(64) not null unique,
     name varchar(100) not null
);

create table if not exists categories (
    id bigint generated by default as identity not null primary key,
    name varchar(100) not null
);

create table if not exists events (
    id bigint generated by default as identity not null primary key,
    title varchar(120) not null,
    annotation varchar(2000) not null,
    description varchar(7000) not null,
    event_date timestamp not null,
    location_lat real not null,
    location_lon real not null,
    category_id bigint references categories(id),
    initiator_id bigint references users(id),
    created_date timestamp not null,
    is_paid boolean not null,
    confirmed_requests bigint not null,
    participant_limit bigint not null,
    is_request_moderation boolean not null,
    published_date timestamp,
    state varchar(32) not null
);

create table if not exists compilations (
    id bigint generated by default as identity primary key not null,
    title varchar(120) not null,
    is_pinned boolean not null
);

create table if not exists event_compilations (
    event_id bigint references events(id),
    compilation_id bigint references  compilations(id),
    primary key (compilation_id, event_id)
);

create table if not exists requests (
    id bigint generated by default as identity primary key not null,
    requester_id bigint references users(id),
    event_id bigint references events(id),
    created_date timestamp not null,
    status varchar(32) not null,
    constraint uk_user_events unique (requester_id, event_id)
);