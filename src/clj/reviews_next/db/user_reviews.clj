(ns reviews-next.db.user-reviews
  (:require
          [clojure.pprint :as pp]
          [clojure.java.jdbc :refer :all]
          [reviews-next.config :as config]))

(def data
  {:from_uid "U1"
   :to_uid "U2"
   :review_id 3})

(defn insert
  "execute query and return lazy sequence"
  ([data] (insert data (config/connection-uri)))
  ([data connection-uri]
   (insert! connection-uri :user_reviews data)))
   ; (try
   ;      do
   ;        (insert! connection-uri :user_reviews data)
   ;        true
   ;   (catch Exception e
   ;     false))))

(defn delete-all
  ([] (delete-all (config/connection-uri)))
  ([connection-uri]
   (db-do-commands connection-uri "delete from user_reviews")))

(defn get-list
  ([] (get-list (config/connection-uri)))
  ([connection-uri]
   (try
      (query connection-uri ["select * from user_reviews"])
     (catch Exception e
      false))))

(defn users-for-review-id
  ([review-id] (users-for-review-id (config/connection-uri)))
  ([review-id connection-uri]
   (db-do-prepared connection-uri "select * from user_reviews where review_id= ?" review-id)))
