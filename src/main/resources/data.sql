DROP TABLE IF EXISTS event ;

CREATE TABLE event (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(250) NOT NULL
);

INSERT INTO event (name) VALUES
  ('start test'),
  ('end test');