CREATE TABLE person (
  id_person serial not null primary key unique,
  name character varying not null,
  age integer,
  country character varying
);

CREATE TABLE telephone (
  id_telephone serial not null primary key unique,
  number character varying not null unique,
  id_person integer not null,
  constraint telephone_person_fk foreign key (id_person) references person (id_person)
);