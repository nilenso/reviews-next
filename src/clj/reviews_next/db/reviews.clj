(ns reviews-next.db.reviews
  (:require
    [clojure.pprint :as pp]
    [ragtime.jdbc :as jdbc]
    [clojure.java.jdbc :refer :all]
    [reviews-next.config :as config]
    [reviews-next.db.migrations :as migrations]))

(defn insert
  "execute insert and return lazy sequence"
  ([data] (insert data (config/connection-uri)))
  ([data connection-uri]
   (try
     (do
       (insert! connection-uri :reviews data)
       true)
     (catch Exception e
       false))))

(defn delete-all
  ([] (delete-all (config/connection-uri)))
  ([connection-uri]
   (db-do-commands connection-uri "delete from reviews")))

(defn get-list
  ([] (get-list (config/connection-uri)))
  ([connection-uri]
   (try
      (query connection-uri ["select * from reviews"])
     (catch Exception e
      false))))
