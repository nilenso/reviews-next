(ns reviews_next.db.db_ops
  (:require
          [clojure.pprint :as pp]
          [clojure.java.jdbc :refer :all]))

(def db
  {:classname   "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname     "reviews_next.db"})

(def data
  {
   :title "report"
   :review_date "20-09-2019"})

(defn insert
  "execute query and return lazy sequence"
  [data]
  (insert! db :reviews data))

(defn delete
  []
  (delete! db :reviews data))

(defn result
  []
  (query db ["select * from reviews"]))
