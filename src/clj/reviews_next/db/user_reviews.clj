(ns reviews-next.db.user-reviews
  (:require
          [clojure.pprint :as pp]
          [clojure.java.jdbc :refer :all]
          [reviews-next.config :as config]))

(def data
  {:from_uid "U1"
   :to_uid "U3"
   :review_id 1})

(defn insert
  "execute query and return lazy sequence"
  ([data] (insert data (config/connection-uri)))
  ([data connection-uri]
   (try
       (do
          (insert! connection-uri :user_reviews data)
          true)
     (catch Exception e
       (str e)))))

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

(defn get-reviews-for-user
  ([user-uid] (get-reviews-for-user user-uid connection-uri-default))
  ([user-uid connection-uri]
   (try
     (query connection-uri ["select * from user_reviews where to_uid=?" user-uid]
            {:row-fn (fn [row] {:from_uid (:from_uid row) :review_id (:review_id row) :id (:review_id row)})})
     (catch Exception e
       false))))

(defn users-for-review-id
  ([review-id] (users-for-review-id review-id (config/connection-uri)))
  ([review-id connection-uri]
   (try
      (let [user-ids (query connection-uri ["select to_uid from user_reviews where review_id=?" review-id] {:row-fn :to_uid})]
       (concat user-ids (query connection-uri ["select distinct from_uid from user_reviews where review_id=?" review-id] {:row-fn :from_uid})))
     (catch Exception e
       (str e)))))

(defn reviews-for-user-id
  ([user-id] (reviews-for-user-id user-id (config/connection-uri)))
  ([user-id connection-uri]
   (try
      (concat
        (query connection-uri ["select review_id from user_reviews where to_uid=?" user-id] {:row-fn :review_id})
        (query connection-uri ["select review_id from user_reviews where from_uid=?" user-id] {:row-fn :review_id}))
     (catch Exception e
       (str e)))))
