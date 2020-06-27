(ns reviews-next.db.user-reviews
  (:require
          [clojure.pprint :as pp]
          [clojure.java.jdbc :refer :all]
          [reviews-next.config :as config]))

(def connection-uri-default (config/connection-uri))
(def connection-uri-test (config/connection-uri "test"))

(def data
  {:from_uid "U1"
   :to_uid "U2"
   :review_id 2})

(defn insert
  "execute query and return lazy sequence"
  ([data] (insert data connection-uri-default))
  ([data connection-uri]
   (try
        do
          (insert! connection-uri :user_reviews data)
          true
     (catch Exception e
       false))))

(defn delete-all
  ([] (delete-all connection-uri-default))
  ([connection-uri]
   (db-do-commands connection-uri "delete from user_reviews")))
