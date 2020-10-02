
create or replace view count_ranges as select count(*) as length from SDB_IP_RANGES;
create or replace view count_ip_routers as select count(*) as length from SDB_IP_ROUTERS;
create or replace view count_routers as select count(*) as length from SDB_ROUTERS;
create or replace view count_incorrect_lines as select count(*) as  length from SDB_INCORRECT_LINE;
create or replace view count_validError as select count(*) as length from VALIDATION;

create or replace view count_invalid_routers as select count(*) as length from SDB_ROUTERS where IS_VALID = 0;
create or replace view count_invalid_ip_routers as select count(*) as length from SDB_IP_ROUTERS where IS_VALID = 0;
create or replace view count_invalid_ip_ranges as select count(*) as length from SDB_IP_RANGES where IS_VALID = 0;
create or replace view count_invalid_object as select count(*) as length from VALIDATION;

create or replace view count_invalid_routers as select count(*) as length from SDB_ROUTERS where IS_VALID = 0;
create or replace view count_invalid_ip_routers as select count(*) as length from SDB_IP_ROUTERS where IS_VALID = 0;
create or replace view count_invalid_ip_ranges as select count(*) as length from SDB_IP_RANGES where IS_VALID = 0;
