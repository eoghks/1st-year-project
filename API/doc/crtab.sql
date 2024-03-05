create table url (
  urlId Integer NOT NULL PRIMARY KEY,
  url VARCHAR(2048) NOT NULL,
  method INTEGER,
  extension INTEGER
);

create table header(
  urlid Integer ,
  key varchar(1024),
  value varchar(1024)
);
alter table header add constraint header_pk primary key(urlid, key);

create table body(
  urlid Integer ,
  key varchar(1024),
  value varchar(1024),
  isList Integer
);
alter table body add constraint body_pk primary key(urlid, key);

--v2 
create table parent(
  urlid Integer PRIMARY KEY,
  parent varchar(1024)
);

create table mapping(
  urlid Integer ,
  colName varchar(1024),
  responseName varchar(1024),
  type Integer
);
alter table mapping add constraint mapping_pk primary key(urlid, colName);

insert into url values(1, 'http://infuser.odcloud.kr/oas/docs', 0 ,1);
insert into body values(1, 'namespace', '15068863/v1', 0);
