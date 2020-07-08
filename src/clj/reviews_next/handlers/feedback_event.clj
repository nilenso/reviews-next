(ns reviews-next.handlers.feedback-event
  (:require [hiccup.core :as h]
            [hiccup.page :as page]
            [clojure.pprint :as pp]
            [reviews-next.db.reviews :as reviews]
            [reviews-next.db.users :as users]
            [reviews-next.db.user-reviews :as user-reviews]
            [reviews-next.db.user-feedback :as user-feedback]
            [ring.middleware.json :refer [wrap-json-params wrap-json-response]]
            [ring.util.response :refer [response created]]))

(defn failed-request [error]
  {:status 404
   :headers {"Content-Type" "text"}
   :body error})

(defn users-list [_request]
 (let [review_id (get-in _request [:params :review_id])
       user-ids (user-reviews/users-for-review-id review_id)
       result (users/users-for-given-ids user-ids)]
   (if (empty? result)
       (failed-request result)
       (response result))))

(defn reviews-list [_request]
  (let [uid (get-in _request [:params :uid])
        reviews-for-uid-list (user-reviews/reviews-for-user-id uid)
        result (reviews/reviews-for-given-ids reviews-for-uid-list)]
    (if (empty? result)
      (failed-request result)
      (response result))))

(defn into-user-feedback
  [_request]
  (let [from_uid (get-in _request [:params "from_uid"])
        to_uid (get-in _request [:params "to_uid"])
        review_id (get-in _request [:params "review_id"])
        feedback (get-in _request [:params "feedback"])
        level (get-in _request [:params "level"])
        is_draft (get-in _request [:params "draft?"])                                 
        result  (user-feedback/insert   {:from_uid from_uid
                                         :to_uid to_uid
                                         :review_id review_id
                                         :feedback feedback
                                         :level level
                                         :is_draft is_draft})]
    (if (= result true)
      (created "Insertion Successful")
      (failed-request (first result)))))
