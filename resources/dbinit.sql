drop schema if exists st_schema cascade;
create schema st_schema;
create table st_schema.students(
    id SERIAL primary key,
    firstname varchar(50) not null,
    lastname varchar(50) not null,
    is_present boolean not null,
    has_answered boolean not null,
    grade int
);
