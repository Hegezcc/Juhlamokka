/* Create Database */
CREATE DATABASE juhlamokka
    WITH
    OWNER = juhlamokka
    ENCODING = 'UTF8'
    LC_COLLATE = 'English_United States.1252'
    LC_CTYPE = 'English_United States.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

/* Create basic table */
CREATE TABLE IF NOT EXISTS products (
  id            SERIAL PRIMARY KEY,   /* Autoincrement */
  name          VARCHAR(255) NOT NULL UNIQUE,
  description   text NULL,
  baseprice     decimal(12, 2),
  quantity      int,
  addedOn       timestamp NOT NULL DEFAULT now(),
  modifiedOn    timestamp NOT NULL DEFAULT now()
);

/* When updated update updatedOn time */
CREATE OR REPLACE FUNCTION update_modified_column()
RETURNS TRIGGER AS $$
BEGIN
   IF row(NEW.*) IS DISTINCT FROM row(OLD.*) THEN
      NEW.modifiedOn = now();
      RETURN NEW;
   ELSE
      RETURN OLD;
   END IF;
END;
$$ language 'plpgsql';
