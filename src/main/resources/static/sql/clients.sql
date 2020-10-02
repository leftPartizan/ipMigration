create user CLIENTS identified by CLIENTS
  DEFAULT TABLESPACE USERS
  TEMPORARY TABLESPACE TEMP;

alter user CLIENTS QUOTA 100M ON USERS;

grant create session to CLIENTS;
grant create table to CLIENTS;

grant insert any table to CLIENTS;
grant update any table to CLIENTS;
grant create sequence to CLIENTS;
grant create procedure to CLIENTS;
GRANT CREATE VIEW TO CLIENTS;
GRANT CREATE TRIGGER TO CLIENTS;
grant alter any trigger to CLIENTS;
grant alter any procedure to CLIENTS;
grant alter any sequence to CLIENTS;
grant delete any table to CLIENTS;

grant select any table to CLIENTS;

grant DROP any sequence to CLIENTS;
grant drop any table to CLIENTS;
grant drop any procedure to CLIENTS;
grant drop any trigger to CLIENTS;
grant drop any view to CLIENTS;
