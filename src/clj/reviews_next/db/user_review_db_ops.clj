(ns reviews-next.db.user-review-db-ops
  (:require
          [clojure.pprint :as pp]
          [clojure.java.jdbc :refer :all]))

(def db
  {:classname   "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname     "reviews_next.db"})

(def data
  {:from_uid "U1"
   :to_uid "U2"
   :review_id 2})

(defn insert
  "execute query and return lazy sequence"
  [data]
  (try
       do
         (insert! db :user_reviews data)
         true
    (catch Exception e
       false)))


(defn get-last-id
  "execute query and return last id"
  []
  (query db ["SELECT id FROM user_reviews ORDER BY id DESC LIMIT 1"]))


(defn delete-all
  []
  (db-do-commands db "delete from user_reviews"))

(defn display-all
  []
  (try
     (query db ["select * from user_reviews"])
    (catch Exception e
     false)))
