--drop table ss_plogcoll;
create table url (
  urlId SERIAL NOT NULL PRIMARY KEY,
  url VARCHAR(2048) NOT NULL,
  method INTEGER,
  extension INTEGER
);
