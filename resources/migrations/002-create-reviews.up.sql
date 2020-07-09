CREATE TABLE reviews (
       id INTEGER PRIMARY KEY,
       title TEXT NOT NULL,
       review_date TEXT NOT NULL,
       CONSTRAINT title_not_empty CHECK (title <> ''),
       CONSTRAINT date_not_empty CHECK (review_date <> '')       
);
