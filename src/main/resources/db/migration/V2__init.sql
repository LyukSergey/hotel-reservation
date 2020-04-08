CREATE TABLE reservation_test_fly_way (
  id                SERIAL,
  first_name        varchar not null,
  last_name         varchar not null,
  room_number       smallint not null,
  start_date        timestamp not null,
  end_date         timestamp not null,

  PRIMARY KEY (id)
);