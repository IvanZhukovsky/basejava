create table resume
(
    uuid      char(36) not null
        primary key,
    full_name text
);

alter table resume
    owner to postgres;

create table contact
(
    id          serial
        primary key,
    type        text     not null,
    value       text     not null,
    resume_uuid char(36) not null
        constraint contact_resume_uuid_fk
            references resume
            on update restrict on delete cascade
);

alter table contact
    owner to postgres;

create table sections
(
    id          serial
        primary key,
    type        text     not null,
    content     text     not null,
    resume_uuid char(36) not null
        constraint sections_resume_uuid_fk
            references resume
            on update restrict on delete cascade
);

alter table sections
    owner to postgres;


