
insert into default_validation_command(id, command, type) VALUES (0,
'declare
    cursor getIncorrectLine is (select * from SDB_INCORRECT_LINE);
  begin
    for cur in getIncorrectLine
      loop
        insert into VALIDATION(TABLE_NAME, INCORRECT_VALUE, TEXT) values
        (''SDB_INCORRECT_LINE'', cur.NUMBER_LINE_TO_FILE,''строка с неизвестным содержимым: '' ||cur.LINE );
      end loop;
  end;', 'убираем строки с неизвестным содержимым');

insert into default_validation_command(id, command, type) VALUES (1,
'BEGIN
  insert into VALIDATION(table_name, incorrect_value, text)
    select ''SDB_ROUTERS'', t.NAME, ''роутеру соответствуют несколько Ip-адресов''
      from SDB_ROUTERS t where IS_VALID = 1 group by NAME having COUNT(NAME) > 1;

  insert into VALIDATION(table_name, incorrect_value, text)
    select ''SDB_IP_ROUTERS'', a,
           ''Ip-адресу соотвествует роутер, которому соответствуют другие Ip-адреса''
      from (select ADDRESS a from SDB_IP_ROUTERS where ID_ROUTER in
        (select ID from SDB_ROUTERS where NAME in
          (select NAME from SDB_ROUTERS
              where IS_VALID = 1 group by NAME having COUNT(NAME) > 1)));

  update SDB_ROUTERS set IS_VALID = 0
  where name in (select NAME from SDB_ROUTERS
    where IS_VALID = 1 group by NAME having COUNT(NAME) > 1);

  update SDB_IP_ROUTERS set IS_VALID = 0
  where ID_ROUTER in (select ID from SDB_ROUTERS
    where NAME in (select NAME from SDB_ROUTERS
      where IS_VALID = 1 group by NAME having COUNT(NAME) > 1));
end;', 'убираем роутеры имена которых в таблице встречаются несколько раз и соответствующие им Ip-адреса');


insert into default_validation_command(id, command, type) VALUES (2,
'BEGIN
  insert into VALIDATION(table_name, incorrect_value, text)
    select ''SDB_IP_ROUTERS'', t.ADDRESS, ''Ip-адрессу соответствуют несколько роутеров''
      from SDB_IP_ROUTERS t
        where IS_VALID = 1 group by ADDRESS having COUNT(ADDRESS) > 1;

  insert into VALIDATION(table_name, incorrect_value, text)
    select ''SDB_ROUTERS'', NAME,
      ''роутеру соответствует Ip-адрес, которому соответствуют несколько роутеров''
      from (select * from SDB_ROUTERS where ID in
        (select ID_ROUTER from SDB_IP_ROUTERS where ADDRESS in
          (select ADDRESS from SDB_IP_ROUTERS
            where IS_VALID = 1 group by ADDRESS having COUNT(ADDRESS) > 1)));

  update SDB_ROUTERS set IS_VALID = 0
  where IS_VALID = 1 and ID in (select ID_ROUTER from SDB_IP_ROUTERS
    where ADDRESS in (select ADDRESS from SDB_IP_ROUTERS
      where IS_VALID = 1 group by ADDRESS having COUNT(ADDRESS) > 1));
  update SDB_IP_ROUTERS set IS_VALID = 0
    where IS_VALID = 1 and ADDRESS in
      (select ADDRESS from SDB_IP_ROUTERS group by ADDRESS having COUNT(ADDRESS) > 1);

end;', 'убираем Ip-адреса имена которых в таблице встречаются несколько раз и соответствующие им роутеры');

insert into DEFAULT_VALIDATION_COMMAND(id, command, type) VALUES (3,
'declare
  cursor getIpWithoutRange is (select * from SDB_IP_ROUTERS where IS_VALID=1 and IP_RANGE_ID=0);
begin
  for cur in getIpWithoutRange
    loop
      insert into VALIDATION (table_name, incorrect_value, text)
      values (''SDB_IP_ROUTERS'', cur.ADDRESS, ''Ip-адрес не находится ни в одной из подсетей'');
      insert into VALIDATION (table_name, incorrect_value, TEXT)
      select ''SDB_IP_ROUTERS'', name, ''роутер имеет Ip-адрес, который не находится ни в одной из подсетей''
      from SDB_ROUTERS where id=cur.ID_ROUTER;
    end loop;
  update SDB_ROUTERS set is_valid = 0
    where ID in (select IP_RANGE_ID from SDB_IP_ROUTERS where IP_RANGE_ID = 0);
  update SDB_IP_ROUTERS set is_valid = 0
    where IP_RANGE_ID = 0;
end;', 'айпи роутеров и роутеры не находящиеся в известных подсетях');

insert into default_validation_command(id, command, type) VALUES (4,
'begin
  insert into VALIDATION(table_name, incorrect_value, text)
    select ''SDB_RANGES'', CIDR, ''данная сеть не содержит Ip-адреса роутеров''
      from SDB_IP_RANGES
      where IS_VALID = 1 and id not in (select IN_RANGE_ID from SDB_IP_RANGES);

  update SDB_IP_RANGES set IS_VALID = 0
    where IS_VALID = 1 and id not in (select IN_RANGE_ID from SDB_IP_RANGES);
end;', 'убираем подсети без Ip-адресов роутеров');
