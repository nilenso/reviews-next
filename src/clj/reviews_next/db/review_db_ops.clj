(ns reviews-next.db.review-db-ops
  (:require
          [clojure.pprint :as pp]
          [clojure.java.jdbc :refer :all]))

(def db
  {:classname   "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname     "reviews_next.db"})

(def data
  {:title "title-trial"
   :review_date "20-09-2020"})

(defn insert
  "execute query and return lazy sequence"
  [data]
  (try
       do
        (insert! db :reviews data)
   (catch Exception e
        false)))

(defn get-last-id
  "execute query and return last id"
  []
  (query db ["SELECT id FROM reviews ORDER BY id DESC LIMIT 1"]))


(defn delete-all
  []
  (db-do-commands db "delete from reviews"))

(defn display-all
  []
  (try
     (query db ["select * from reviews"])
    (catch Exception e
     false)))

(defn insert-and-get-last-id
  [data]
  (try
    (with-db-transaction [t-con db]
                         (insert! t-con :reviews data)
                         (query t-con ["SELECT id FROM reviews ORDER BY id DESC LIMIT 1"]))
    (catch Exception e
           (str (.getMessage e)))))
