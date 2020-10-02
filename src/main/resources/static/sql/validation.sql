CREATE SEQUENCE validation_id_auto START WITH 1;

create or replace procedure reload_id_sequence_for_valid
  authid current_user
is
begin
  execute immediate 'drop sequence VALIDATION_ID_AUTO';
  execute immediate 'CREATE SEQUENCE validation_id_auto START WITH 1';
  commit;
end;

create or replace trigger auto_increment_valid_table
  before insert on VALIDATION
  for each row
begin
  select validation_id_auto.nextval
         into :NEW.ID
  from DUAL;
end;

create or replace procedure EXECUTE_COMMANDS(status out number)
  authid current_user
as
  cursor commands is (select * from (select * from COMMAND_LINES order by COMMAND_LINES.ID));
  count_commands number;
  id_command number;
begin
  status:= -1;
  id_command := -1;
  delete from VALIDATION;
  select c into count_commands FROM (select count(*) c from COMMAND_LINES);

  if count_commands = 0 then
    insert into COMMAND_LINES select d.ID, d.COMMAND, d.TYPE from DEFAULT_VALIDATION_COMMAND d;
  end if;
  RELOAD_ID_SEQUENCE_FOR_VALID();

  for cur in commands
    loop
      id_command:= cur.ID;
      execute immediate cur.COMMAND;
    end loop;
  exception
    when OTHERS then
      status:= id_command;
end;
