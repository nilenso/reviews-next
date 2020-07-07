(ns reviews-next.db.user-feedback
  (:require
          [clojure.pprint :as pp]
          [clojure.java.jdbc :refer :all]
          [reviews-next.config :as config]))

(def data
  {:from_uid "U3"
   :to_uid "U2"
   :review_id 2
   :feedback "Great Job"
   :level 6.1})

(defn insert
  "execute query and return lazy sequence"
  ([data] (insert data (config/connection-uri)))
  ([data connection-uri]
   (try
        (do
          (insert! connection-uri :user_feedback data)
          true)
     (catch Exception e
       (str e)))))

(defn delete-all
  ([] (delete-all (config/connection-uri)))
  ([connection-uri]
   (db-do-commands connection-uri "delete from user_feedback")))
