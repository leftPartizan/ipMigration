
CREATE SEQUENCE OBJECT_ID_AUTO_INCREMENT START WITH 0 increment by 1 minvalue 0;

create or replace procedure reload_OBJECT_AUTO_INCREMENT
  authid current_user
as
begin
  execute immediate 'drop sequence OBJECT_ID_AUTO_INCREMENT';
  execute immediate 'CREATE SEQUENCE OBJECT_ID_AUTO_INCREMENT START WITH 0 increment by 1 minvalue 0';
end;


create or replace procedure delete_invalid_data
is
begin
  delete from SDB_ROUTERS where IS_VALID = 0;
  delete from SDB_IP_ROUTERS where IS_VALID = 0;
  delete from SDB_IP_RANGES where IS_VALID = 0;
  delete from SDB_INCORRECT_LINE;
end;


create or replace procedure reload_id_sdb
  authid current_user
is
    cursor ranges is (select * from (select * from SDB_IP_RANGES order by IN_RANGE_ID));
    cursor ips is (select distinct ADDRESS from SDB_IP_ROUTERS);
    next_id number;
  begin
  for range in ranges
    loop
      next_id := OBJECT_ID_AUTO_INCREMENT.nextval;
      update SDB_IP_RANGES set new_id = next_id  where id = range.ID;
      update SDB_IP_RANGES set NEW_IN_RANGE_ID = next_id where IN_RANGE_ID = range.ID;
      update SDB_IP_ROUTERS set NEW_IP_RANGE_ID = next_id where IP_RANGE_ID = range.ID;
    end loop;

  for ip in ips
    loop
      next_id:= OBJECT_ID_AUTO_INCREMENT.nextval;
      update SDB_IP_ROUTERS set new_id= next_id where ADDRESS = ip.ADDRESS and rownum = 1;
      for routers in (select ID from SDB_ROUTERS where id in
                    (select ID_ROUTER from SDB_IP_ROUTERS where ADDRESS = ip.ADDRESS) )
        loop
          update SDB_ROUTERS set NEW_ID_IP = next_id where ID = routers.ID;
        end loop;
    end loop;

  for routers in (select distinct NAME from SDB_ROUTERS)
    loop
      next_id := OBJECT_ID_AUTO_INCREMENT.nextval;
      update SDB_ROUTERS set NEW_ID = next_id where NAME = routers.NAME;
    end loop;
end;

create or replace procedure load_data_from_sdb
  authid current_user
is
begin
  delete_invalid_data();
  reload_id_sdb();
  insert into OBJECT  select t.NEW_ID, 2, t.CIDR, t.NEW_IN_RANGE_ID from SDB_IP_RANGES t where NEW_ID is not null ;
  insert into PARAMS  select t.NEW_ID, 2, 'public' from SDB_IP_RANGES t where NEW_ID is not null;
  insert into PARAMS  select t.NEW_ID, 2, 'yes' from SDB_IP_RANGES t where NEW_ID is not null;

  insert into OBJECT  select t.NEW_ID, 1, t.ADDRESS, t.NEW_IP_RANGE_ID from SDB_IP_ROUTERS t where NEW_ID is not null;
  insert into PARAMS  select t.NEW_ID, 2, 'public' from SDB_IP_ROUTERS t where NEW_ID is not null;
  insert into PARAMS  select t.NEW_ID, 2, 'yes' from SDB_IP_ROUTERS t where NEW_ID is not null;

  for r in (select distinct NEW_ID from SDB_ROUTERS where NEW_ID is not null)
    loop
      insert into OBJECT  select t.NEW_ID, 3, t.NAME, null from SDB_ROUTERS t where NEW_ID=r.NEW_ID and ROWNUM = 1;
      insert into PARAMS  select t.NEW_ID, 2, 'no' from SDB_IP_ROUTERS t where NEW_ID = r.NEW_ID and rownum=1;
      for r2 in (select * from SDB_ROUTERS where NEW_ID = r.NEW_ID)
          loop
              insert into REFERENCE values (r2.NEW_ID, 1, r2.NEW_ID_IP);
          end loop;
      end loop;
end;
