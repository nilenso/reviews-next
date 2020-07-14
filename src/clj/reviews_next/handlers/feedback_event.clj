(ns reviews-next.handlers.feedback-event
  (:require [hiccup.core :as h]
            [hiccup.page :as page]
            [clojure.pprint :as pp]
            [reviews-next.db.reviews :as reviews]
            [reviews-next.db.users :as users]
            [reviews-next.db.user-reviews :as user-reviews]
            [reviews-next.db.user-feedback :as user-feedback]
            [ring.middleware.json :refer [wrap-json-params wrap-json-response]]
            [ring.util.response :refer [response]]))

; change publish ito to create
(defn insert-into-db [_request]
  (let [title (get-in _request [:params "title"])
        date (get-in _request [:params "review_date"])
        from_uid (get-in _request [:params "from_uid"])
        participants (get-in _request [:params "participants"])
        review_id (reviews/insert-and-get-last-id {:title title
                                                   :review_date date})]
    (doseq [participant participants]
       (user-reviews/insert {:from_uid from_uid :to_uid participant :review_id review_id}))
    (response (str "Posted"))))

(defn users-list [_request]
 (let [review_id (get-in _request [:params :review_id])
       user-ids (user-reviews/users-for-review-id review_id)]
   (response (users/users-for-given-ids user-ids))))

(defn feeback-details-from-id [_request]
  (let [review_id (get-in _request [:params :review_id])
        feedback-details (user-feedback/feedback-for-given-review-id review_id)]
    (response feedback-details)))

(defn create_user_review_map [user_id review_id] 
  {:user_id user_id :review_id review_id})

(defn join_user_review_table [user_review_id]
  (let [user_id (:from_uid user_review_id)
        review_id (:review_id user_review_id)
        user (users/users-for-given-id user_id)
        review (reviews/review-for-given-id review_id)
        user-review {:user user :review review}]
    user-review))

(defn get-user-and-review-ids [_request]
  (let [reviews-list (user-reviews/get-reviews-for-user "U2")
        user_review_list (map join_user_review_table reviews-list)]
    (response  user_review_list)))

(defn reviews-list [_request]
   (response (reviews/get-list)))

(defn into-user-feedback
  [_request]
  (let [from_uid (get-in _request [:params "from_uid"])
        to_uid (get-in _request [:params "to_uid"])
        review_id (get-in _request [:params "review_id"])
        feedback (get-in _request [:params "feedback"])
        level (get-in _request [:params "level"])]
    (response (str user-feedback/insert {:from_uid from_uid
                                         :to_uid to_uid
                                         :review_id review_id
                                         :feedback feedback
                                         :level level}))))
