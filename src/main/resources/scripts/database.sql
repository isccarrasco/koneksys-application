CREATE TABLE person (
  id_person serial not null primary key unique,
  name character varying not null,
  age integer,
  country character varying
);