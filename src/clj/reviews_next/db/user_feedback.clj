(ns reviews-next.db.user-feedback
  (:require
          [clojure.pprint :as pp]
          [clojure.java.jdbc :refer :all]
          [reviews-next.config :as config]))

(def data
  {:from_uid "U1"
   :to_uid "U3"
   :review_id 3
   :feedback "Fab job."
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

(defn get-list
  ([] (get-list (config/connection-uri)))
  ([connection-uri]
   (try
     (query connection-uri ["select * from user_feedback"])
     (catch Exception e
       (str e)))))


(defn feedbacks-from-uid
  ([from-uid] (feedbacks-from-uid from-uid (config/connection-uri)))
  ([from-uid connection-uri]
    (try
      (query connection-uri ["select * from user_feedback where is_draft=0 and from_uid=?" from-uid])
     (catch Exception e
      (str e)))))

(defn feedback-for-given-review-id
  ([review-id] (feedback-for-given-review-id review-id (config/connection-uri)))
  ([review-id connection-uri]
    (try
      (query connection-uri ["select * from user_feedback where is_draft=0 and review_id=?" review-id])
     (catch Exception e
      (str e)))))

(defn get-reviews-by-user
  ([user-uid] (get-reviews-by-user user-uid (config/connection-uri)))
  ([user-uid connection-uri]
   (try
     (query connection-uri ["select * from user_feedback where is_draft=0 and from_uid=?" user-uid]
            {:row-fn (fn [row] {:to_uid (:to_uid row)
                                :review_id (:review_id row)
                                :id (:id row)
                                :feedback (:feedback row)
                                :level (:level row)
                                :is_draft (:is_draft row)})})
     (catch Exception e
       false))))

(defn get-draft-reviews-by-user
  ([user-uid] (get-draft-reviews-by-user user-uid (config/connection-uri)))
  ([user-uid connection-uri]
   (try
     (query connection-uri ["select * from user_feedback where is_draft=1 and from_uid=?" user-uid]
            {:row-fn (fn [row] {:to_uid (:to_uid row)
                                :review_id (:review_id row)
                                :id (:id row)
                                :feedback (:feedback row)
                                :level (:level row)
                                :is_draft (:is_draft row)})})
     (catch Exception e
       false))))

(defn publish-draft-feedback
  ([feedback-id] (publish-draft-feedback feedback-id (config/connection-uri)))
  ([feedback-id connection-uri]
   (try
     (update! connection-uri :user_feedback {:is_draft 0} ["id = ?" feedback-id])
     (catch Exception e
       (str e)))))

(defn delete-feedback
  ([feedback-id] (delete-feedback feedback-id (config/connection-uri)))
  ([feedback-id connection-uri]
   (try
     (delete! connection-uri :user_feedback ["id = ?" feedback-id])
     (catch Exception e
       (str e)))))