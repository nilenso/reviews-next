CREATE TABLE user_reviews (
       id INTEGER PRIMARY KEY,
       from_uid TEXT NOT NULL,
       to_uid TEXT NOT NULL,
       review_id TEXT NOT NULL,
       FOREIGN KEY (from_uid) REFERENCES USERS(id),
       FOREIGN KEY (to_uid) REFERENCES USERS(id),
       FOREIGN KEY (review_id) REFERENCES REVIEWS(id)
);
