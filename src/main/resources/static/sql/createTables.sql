
create table object_type
(
  id number
    constraint object_type_pk
      primary key,
  name varchar2(32)
);

INSERT INTO OBJECT_TYPE (ID, NAME) values (1, 'Ip Address');
INSERT INTO OBJECT_TYPE (ID, NAME) values (2, 'Ip Range');
INSERT INTO OBJECT_TYPE (ID, NAME) values (3, 'Router');


create table attribute
(
  id number
    constraint attribute_pk
      primary key,
  name varchar2(32),
  type varchar2(124)
);

insert into attribute(id, name, type) VALUES (1, 'Reference to IP', '[True/False]');
insert into attribute(id, name, type) VALUES (2, 'IP type', '[private/public]');

create table Attributes_object_type
(
  object_type_id number,
  attribute_id number,
  constraint fk_object_type
    foreign key (object_type_id)
      references object_type(ID)
        on delete cascade,
  constraint fk_attr
    foreign key (attribute_id)
      references ATTRIBUTE(ID)
        on delete cascade

);

insert into Attributes_object_type(OBJECT_TYPE_ID, attribute_id) VALUES (1, 1);
insert into Attributes_object_type(OBJECT_TYPE_ID, attribute_id) VALUES (1, 2);
insert into Attributes_object_type(OBJECT_TYPE_ID, attribute_id) VALUES (2, 2);
insert into Attributes_object_type(OBJECT_TYPE_ID, attribute_id) VALUES (2, 2);
insert into Attributes_object_type(OBJECT_TYPE_ID, attribute_id) VALUES (3, 1);


create table object(
    id number
      constraint pk_object
        primary key,
    object_type_id number,
    name varchar2(32),
    parent_id int,
    constraint fk_object_object_type
      foreign key (object_type_id)
        references OBJECT_TYPE(id)
          on delete set null
);

create table params(
     object_id number,
     attribute_id number,
     value varchar2(32),
     constraint fk_params_object_type
         foreign key (object_id)
           references OBJECT(ID)
            on delete cascade,
     constraint fk_params_attribute
         foreign key (attribute_id)
           references ATTRIBUTE(ID)
             on delete cascade
);

create table reference (
     object_id number,
     attribute_id number,
     refer number,
     constraint fk_reference_object_id
         foreign key (object_id)
           references OBJECT(ID)
             on delete cascade,
     constraint fk_reference_attribute_id
         foreign key (attribute_id)
           references ATTRIBUTE(ID)
             on delete cascade
);

create table VALIDATION
(
  ID int
    constraint validation_pk
      primary key,
  TABLE_NAME       VARCHAR2(64),
  INCORRECT_VALUE VARCHAR2(64),
  TEXT             VARCHAR2(512)
);

create table COMMAND_LINES
(
  ID      NUMBER(10) not null
    constraint COMMAND_LINES_PK
    primary key,
  COMMAND VARCHAR2(3024 char),
  TYPE    VARCHAR2(1024 char)
);

create table DEFAULT_VALIDATION_COMMAND
(
  ID      NUMBER not null
    constraint DEFAULT_VALIDATION_COMMAND_PK
      primary key,
  COMMAND VARCHAR2(3024),
  TYPE    VARCHAR2(1024)
);

create or replace procedure DELETE_INVALID_DATA
authid current_user
is
begin
  delete from SDB_INCORRECT_LINE;
  delete from SDB_ROUTERS where IS_VALID = 0;
  delete from SDB_IP_ROUTERS where IS_VALID = 0;
  delete from SDB_IP_RANGES where IS_VALID = 0;
end;

create or replace procedure clear_Sdb_Tables
  authid current_user
is
begin
  delete from SDB_INCORRECT_LINE;
  delete from SDB_ROUTERS;
  delete from SDB_IP_ROUTERS;
  delete from SDB_IP_RANGES;
end;

create or replace procedure reload_obj_table
authid current_user
is
begin
  delete from object;
  RELOAD_OBJECT_AUTO_INCREMENT();
end;
