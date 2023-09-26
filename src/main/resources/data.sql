create table if not exists persistent_logins (
  username varchar(100) not null,
  series varchar(64) primary key,
  token varchar(64) not null,
  last_used timestamp not null
);

INSERT IGNORE INTO privilege (name, id) VALUES ('READ_PRIVILEGE', 1);
INSERT IGNORE INTO privilege (name, id) VALUES ('UPDATE_PRIVILEGE', 2);
INSERT IGNORE INTO privilege (name, id) VALUES ('WRITE_PRIVILEGE', 3);
INSERT IGNORE INTO privilege (name, id) VALUES ('DELETE_PRIVILEGE', 4);

INSERT IGNORE INTO roles (id, role_name) VALUES
(5, 'ROLE_USER'),
(6, 'ROLE_ADMIN'),
(7, 'ROLE_SECURED');

insert IGNORE into roles_privileges (role_id, privilege_id) values (5, 1);
insert IGNORE into roles_privileges (role_id, privilege_id) values (6, 1);
insert IGNORE into roles_privileges (role_id, privilege_id) values (6, 2);
insert IGNORE into roles_privileges (role_id, privilege_id) values (6, 3);
insert IGNORE into roles_privileges (role_id, privilege_id) values (7, 1);
insert IGNORE into roles_privileges (role_id, privilege_id) values (7, 2);
insert IGNORE into roles_privileges (role_id, privilege_id) values (7, 3);
insert IGNORE into roles_privileges (role_id, privilege_id) values (7, 4);