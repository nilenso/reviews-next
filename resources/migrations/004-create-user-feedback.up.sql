CREATE TABLE user_feedback (
       id INTEGER PRIMARY KEY,
       from_uid TEXT NOT NULL,
       to_uid TEXT NOT NULL,
       review_id TEXT NOT NULL,
       feedback TEXT NOT NULL,
       level REAL,
       FOREIGN KEY (from_uid) REFERENCES USERS(id),
       FOREIGN KEY (to_uid) REFERENCES USERS(id),
       FOREIGN KEY (review_id) REFERENCES REVIEWS(id)
);
