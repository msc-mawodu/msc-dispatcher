-- DROP TABLE IF EXISTS event;
-- DROP TABLE IF EXISTS profiling;

CREATE TABLE event (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(250) NOT NULL
);

CREATE TABLE profiling (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  dispatched bit default 0,
  metrics LONGVARCHAR
);
