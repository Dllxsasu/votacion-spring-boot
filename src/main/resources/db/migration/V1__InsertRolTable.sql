create table IF NOT EXISTS roles (id  bigserial not null, name varchar(60), primary key (id));
INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');