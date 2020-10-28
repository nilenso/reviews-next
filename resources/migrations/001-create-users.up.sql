CREATE TABLE users (
    id integer PRIMARY KEY AUTOINCREMENT,
    name text NOT NULL,
    email text NOT NULL,
    google_id text NOT NULL,
    image_url text NOT NULL,
    created_at text NOT NULL,
    updated_at text NOT NULL,
    CONSTRAINT name_not_empty CHECK (name <> ''),
    CONSTRAINT email_not_empty CHECK (email <> ''),
    CONSTRAINT google_id_not_empty CHECK (email <> '')
);

