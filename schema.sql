/* Create Database */
CREATE DATABASE juhlamokka
    WITH
    OWNER = juhlamokka
    ENCODING = 'UTF8'
    LC_COLLATE = 'English_United States.1252'
    LC_CTYPE = 'English_United States.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

/* Create products table */
CREATE TABLE IF NOT EXISTS products (
  id            SERIAL PRIMARY KEY,   /* Autoincrement */
  name          varchar(255) NOT NULL UNIQUE,
  description   text NULL,
  basePrice     decimal(12, 2),
  amount	    int,
  unit			varchar(255) NOT NULL,
  addedOn       timestamp NOT NULL DEFAULT now(),
  modifiedOn    timestamp NOT NULL DEFAULT now()
);

/* Create users table */
CREATE TABLE IF NOT EXISTS users (
  id            SERIAL PRIMARY KEY,   /* Autoincrement */
  name          varchar(255) NOT NULL UNIQUE,
  password      varchar(255) NOT NULL,
  description   text NULL,
  isAdmin		BOOLEAN NOT NULL DEFAULT FALSE,
  isLocked	    BOOLEAN NOT NULL DEFAULT FALSE,
  addedOn       timestamp NOT NULL DEFAULT now(),
  modifiedOn    timestamp NOT NULL DEFAULT now()
);

/* Create clients table */
CREATE TABLE IF NOT EXISTS clients (
  id            SERIAL PRIMARY KEY,   /* Autoincrement */
  name          varchar(255) NOT NULL UNIQUE,
  description   text NULL,
  addedOn       timestamp NOT NULL DEFAULT now(),
  modifiedOn    timestamp NOT NULL DEFAULT now()
);

/* Create transactions table */
CREATE TABLE IF NOT EXISTS transactions (
  id            SERIAL PRIMARY KEY,   /* Autoincrement */
  name			varchar(255),
  userid        integer REFERENCES users(id) ON DELETE SET NULL,
  clientid      integer REFERENCES clients(id) ON DELETE SET NULL,
  description   text NULL,
  productid     integer REFERENCES products(id) ON DELETE SET NULL,
  amount	    int,
  price         decimal(12, 2),
  addedOn       timestamp NOT NULL DEFAULT now(),
  modifiedOn    timestamp NOT NULL DEFAULT now()
);

/* Create logins table */
CREATE TABLE IF NOT EXISTS logins (
  id            		SERIAL PRIMARY KEY,   /* Autoincrement */
  userid        		integer REFERENCES users(id) ON DELETE SET NULL,
  name					varchar(255),
  ipaddress     		inet,
  issuccessful      	BOOLEAN NOT NULL DEFAULT FALSE,
  authenticationkey		varchar(32),
  addedOn       		timestamp NOT NULL DEFAULT now(),
  modifiedOn    		timestamp NOT NULL DEFAULT now()
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

/* Create triggers for modifiedOn */
CREATE TRIGGER update_modified_column BEFORE UPDATE ON products FOR EACH ROW EXECUTE PROCEDURE update_modified_column();
CREATE TRIGGER update_modified_column BEFORE UPDATE ON users FOR EACH ROW EXECUTE PROCEDURE update_modified_column();
CREATE TRIGGER update_modified_column BEFORE UPDATE ON clients FOR EACH ROW EXECUTE PROCEDURE update_modified_column();
CREATE TRIGGER update_modified_column BEFORE UPDATE ON transactions FOR EACH ROW EXECUTE PROCEDURE update_modified_column();
