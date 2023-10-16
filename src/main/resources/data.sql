create table if not exists persistent_logins (
  username varchar(100) not null,
  series varchar(64) primary key,
  token varchar(64) not null,
  last_used timestamp not null
);

MERGE INTO privilege (name, id, version) KEY(id) VALUES ('READ_PRIVILEGE', 1, 1);
MERGE INTO privilege (name, id, version) KEY(id) VALUES ('UPDATE_PRIVILEGE', 2, 1);
MERGE INTO privilege (name, id, version) KEY(id) VALUES ('WRITE_PRIVILEGE', 3, 1);
MERGE INTO privilege (name, id, version) KEY(id) VALUES ('DELETE_PRIVILEGE', 4, 1);


MERGE INTO roles (id, role_name, version) VALUES
(5, 'ROLE_USER',1),
(6, 'ROLE_ADMIN',1),
(7, 'ROLE_SECURED',1);

MERGE INTO roles_privileges (role_id, privilege_id) values (5, 1);
MERGE INTO roles_privileges (role_id, privilege_id) values (6, 1);
MERGE INTO roles_privileges (role_id, privilege_id) values (6, 2);
MERGE INTO roles_privileges (role_id, privilege_id) values (6, 3);
MERGE INTO roles_privileges (role_id, privilege_id) values (7, 1);
MERGE INTO roles_privileges (role_id, privilege_id) values (7, 2);
MERGE INTO roles_privileges (role_id, privilege_id) values (7, 3);
MERGE INTO roles_privileges (role_id, privilege_id) values (7, 4);