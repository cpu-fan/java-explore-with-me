drop table if exists apps, hits cascade;

create table if not exists apps (
    id bigint generated by default as identity not null primary key,
    name varchar(128) not null unique
);

create table if not exists hits (
    id bigint generated by default as identity not null primary key,
    app_id bigint not null references apps(id),
    uri varchar(1024) not null,
    ip varchar(16) not null,
    timestamp timestamp
);