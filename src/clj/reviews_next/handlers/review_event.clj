(ns reviews-next.handlers.review-event
  (:require [hiccup.core :as h]
            [hiccup.page :as page]
            [clojure.pprint :as pp]
            [reviews-next.db.review-db-ops :as review_db_ops]
            [reviews-next.db.user-db-ops :as user_db_ops]
            [reviews-next.db.user-review-db-ops :as user_review_db_ops]
            [ring.middleware.json :refer [wrap-json-params wrap-json-response]]
            [ring.util.response :refer [response]]))

(defn publish-into-reviews [_request]
  (let [title (get-in _request [:params "title"])
        date (get-in _request [:params "review_date"])]
    (response (str (review_db_ops/insert {:title title :review_date date})))))

(defn publish-into-user-reviews [_request]
  (let [title (get-in _request [:params "title"])
        date (get-in _request [:params "review_date"])
        from_uid (get-in _request [:params "from_uid"])
        participants (get-in _request [:params "participants"])
        temp-results (vector nil)
        review_id (get
                       (nth
                            (review_db_ops/insert-and-get-last-id {:title title
                                                                   :review_date date}) 0) :id)]
    (doseq [participant participants]
       (user_review_db_ops/insert {:from_uid from_uid :to_uid participant :review_id review_id}))
    (response (str "posted"))))

(defn participants-from-users [_request]
 (response (user_db_ops/display-all)))
