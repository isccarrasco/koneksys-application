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

CREATE TABLE person_known
(
  id_person integer not null,
  id_known integer not null,
  constraint person_person_fk foreign key (id_person) references person (id_person),
  constraint person_known_fk foreign key (id_known) references person (id_person)
);

