# --- First database schema

# --- !Ups

create table scores (
  uuid                      varchar(255) not null primary key,
  name                      varchar(255) not null,
  score                     int not null
);

# --- !Downs

drop table if exists scores;
