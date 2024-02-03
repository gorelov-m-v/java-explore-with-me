CREATE TABLE IF NOT EXISTS stats (
  id INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  app VARCHAR(100) NOT NULL,
  uri VARCHAR(255) NOT NULL,
  ip VARCHAR(15) NOT NULL,
  date_time timestamp NOT NULL,
  CONSTRAINT pk_stats PRIMARY KEY (id)
);