create table SDB_ROUTERS
(
  ID       NUMBER(10) not null
    primary key,
  IS_VALID NUMBER(10) not null,
  NAME     VARCHAR2(255 char),
  NEW_ID      NUMBER(10),
  NEW_ID_IP      NUMBER(10)
);

create table SDB_IP_ROUTERS
(
  ID_ROUTER   NUMBER(10) not null
    primary key,
  ADDRESS     VARCHAR2(255 char),
  IP_RANGE_ID NUMBER(10),
  IS_VALID    NUMBER(10) not null,
  NEW_ID      NUMBER(10),
  NEW_IP_RANGE_ID NUMBER(10)
);

create table SDB_IP_RANGES
(
  ID          NUMBER(10) not null
    primary key,
  CIDR        VARCHAR2(255 char),
  IN_RANGE_ID NUMBER(10),
  IS_VALID    NUMBER(10) not null,
  NEW_ID      NUMBER(10),
  NEW_IN_RANGE_ID NUMBER(10)

);

create table SDB_INCORRECT_LINE
(
  NUMBER_LINE_TO_FILE NUMBER(10) not null
    primary key,
  LINE                VARCHAR2(255 char)
);