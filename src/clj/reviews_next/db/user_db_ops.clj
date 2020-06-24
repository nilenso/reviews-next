(ns reviews-next.db.user-db-ops
  (:require
          [clojure.pprint :as pp]
          [clojure.java.jdbc :refer :all]))

(def db
  {:classname   "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname     "reviews_next.db"})

(def data
  {:id "U4"
   :name "PHI"
   :email "phi@gmail.com"})

(defn insert
  "execute query and return lazy sequence"
  [data]
  (try
    (insert! db :users data)
   (catch Exception e
        false)))

(defn delete-all
  []
  (db-do-commands db "delete from users"))

(defn display-all
  []
  (try
     (query db ["select * from users"])
    (catch Exception e
     false)))
