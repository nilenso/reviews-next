(ns reviews-next.db.user-feedback
  (:require
          [clojure.pprint :as pp]
          [clojure.java.jdbc :refer :all]
          [reviews-next.config :as config]))

(def data
  {:from_uid "U1"
   :to_uid "U3"
   :review_id 2
   :feedback "Great Job"
   :level 6.1
   :is_draft 0})

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

(defn feedbacks-from-uid
  ([from-uid] (feedbacks-from-uid from-uid (config/connection-uri)))
  ([from-uid connection-uri]
    (try
      (query connection-uri ["select * from user_feedback where is_draft=0 and from_uid=?" from-uid])
     (catch Exception e
      (str e)))))