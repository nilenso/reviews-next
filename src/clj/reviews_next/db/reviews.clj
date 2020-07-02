(ns reviews-next.db.reviews
  (:require
    [clojure.pprint :as pp]
    [ragtime.jdbc :as jdbc]
    [clojure.java.jdbc :refer :all]
    [reviews-next.config :as config]
    [reviews-next.db.migrations :as migrations]))

(def connection-uri-default (config/connection-uri))

(defn insert
  "execute insert and return lazy sequence"
  ([data] (insert data connection-uri-default))
  ([data connection-uri]
   (try
     (do
       (insert! connection-uri :reviews data)
       true)
     (catch Exception e
       false))))

(defn delete-all
  ([] (delete-all connection-uri-default))
  ([connection-uri]
   (db-do-commands connection-uri "delete from reviews")))

(defn get-list
  ([] (get-list connection-uri-default))
  ([connection-uri]
   (try
      (query connection-uri ["select * from reviews"])
     (catch Exception e
      false))))
