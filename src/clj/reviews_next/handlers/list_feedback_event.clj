(ns reviews-next.handlers.list-feedback-event
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


(defn feedback-from-user [_request]
 (let [from-uid (get-in _request [:params :from_uid])
       feedbacks-list (user-feedback/feedbacks-from-uid from-uid)
       feedback-with-user-details (map users/concat-user-details feedbacks-list)
       feedback-with-review-details (map reviews/concat-review-details feedback-with-user-details)]
   (if (empty? feedback-with-review-details)
       (failed-request feedback-with-review-details)
       (response feedback-with-review-details))))

(defn delete-from-user-feedback [_request]
 (let [feedback-id (get-in _request [:params "feedback_id"])
       result (user-feedback/delete-feedback feedback-id)]
   (if (empty? result)
       (failed-request result)
       (created "Deleted"))))