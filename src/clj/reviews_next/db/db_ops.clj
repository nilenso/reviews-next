(ns reviews-next.db.db_ops
  (:require
          [clojure.pprint :as pp]
          [clojure.java.jdbc :refer :all]))

(def db
  {:classname   "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname     "reviews_next.db"})

(def data
  {:title "title"
   :review_date "20-09-2019"})

(defn insert
  "execute query and return lazy sequence"
  [data]
  (try
       do
        (insert! db :reviews data)
        true
   (catch Exception e
        false)))

(defn delete-all
  []
  (db-do-commands db "delete from reviews"))

(defn display-all
  []
  (try
     (query db ["select * from reviews"])
    (catch Exception e
     false)))
