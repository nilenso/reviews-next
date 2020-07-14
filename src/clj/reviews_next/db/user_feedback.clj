(ns reviews-next.db.user-feedback
  (:require
          [clojure.pprint :as pp]
          [clojure.java.jdbc :refer :all]
          [reviews-next.config :as config]))

(def connection-uri-default (config/connection-uri))
(def connection-uri-test (config/connection-uri "test"))

(def data
  {:from_uid "U3"
   :to_uid "U2"
   :review_id 1
   :feedback "Great Job"
   :level 6.1})

(defn insert
  "execute query and return lazy sequence"
  ([data] (insert data connection-uri-default))
  ([data connection-uri]
   (try
        do
          (insert! connection-uri :user_feedback data)
          true
     (catch Exception e
       false))))

(defn get-list
  ([] (get-list connection-uri-default))
  ([connection-uri]
   (try
     (query connection-uri ["select * from user_feedback"])
     (catch Exception e
       false))))

(defn delete-all
  ([] (delete-all connection-uri-default))
  ([connection-uri]
   (db-do-commands connection-uri "delete from user_feedback")))


(defn feedback-for-given-review-id
  ([review-id] (feedback-for-given-review-id review-id connection-uri-default))
  ([review-id connection-uri]
   (try
     (query connection-uri ["select * from user_feedback where review_id=?" review-id])
     (catch Exception e
       (str e)))))
