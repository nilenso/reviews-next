CREATE TABLE users (
       id INTEGER PRIMARY KEY,
       name TEXT NOT NULL,
       email TEXT NOT NULL,
       CONSTRAINT name_not_empty CHECK (name <> ''),
       CONSTRAINT email_not_empty CHECK (email <> '')
);
