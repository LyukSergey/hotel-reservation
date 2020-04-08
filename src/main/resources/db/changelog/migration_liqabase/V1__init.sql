CREATE TABLE reservation (
  id                SERIAL,
  first_name        varchar not null,
  last_name         varchar not null,
  room_number       smallint not null,
  start_date        timestamp not null,
  end_date         timestamp not null,

  PRIMARY KEY (id)
);

INSERT INTO public.reservation (first_name, last_name, room_number, start_date, end_date)
VALUES ('first_name1', 'last_name1', 1, '2020-02-11', '2020-02-13');
INSERT INTO public.reservation (first_name, last_name, room_number, start_date, end_date)
VALUES ('first_name2', 'last_name2', 2, '2020-02-12', '2020-02-18');
INSERT INTO public.reservation (first_name, last_name, room_number, start_date, end_date)
VALUES ('first_name3', 'last_name3', 3, '2020-02-13', '2020-02-21');
INSERT INTO public.reservation (first_name, last_name, room_number, start_date, end_date)
VALUES ('first_name4', 'last_name4', 4, '2020-02-14', '2020-02-22');

