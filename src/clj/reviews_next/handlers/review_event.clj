(ns reviews-next.handlers.review-event
  (:require [hiccup.core :as h]
            [hiccup.page :as page]
            [clojure.pprint :as pp]
            [reviews-next.db.reviews :as reviews]
            [reviews-next.db.users :as users]
            [reviews-next.db.user-reviews :as user-reviews]
            [ring.middleware.json :refer [wrap-json-params wrap-json-response]]
            [ring.util.response :refer [response]]))

; change publish ito to create
(defn insert-into-db [_request]
  (let [title (get-in _request [:params "title"])
        date (get-in _request [:params "review_date"])
        description (get-in _request [:params "review_description"])
        from_uid (get-in _request [:params "from_uid"])
        participants (get-in _request [:params "participants"])
        review_id (reviews/insert-and-get-last-id {:title title
                                                   :review_date date
                                                   :review_description description})]
    (doseq [participant participants]
      (user-reviews/insert {:from_uid from_uid :to_uid participant :review_id review_id}))
    (response (str "Posted"))))

(defn users-list [_request]
  (response (users/get-users)))