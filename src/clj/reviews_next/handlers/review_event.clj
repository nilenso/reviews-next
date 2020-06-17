(ns reviews-next.handlers.review-event
  (:require [hiccup.core :as h]
            [hiccup.page :as page]
            [clojure.pprint :as pp]
            ; [reviews-next.config :as config]
            [reviews-next.db.db_ops :as db_ops]
            [ring.middleware.json :refer [wrap-json-params]]
            [ring.util.response :refer [response]]))

(defn index [_request]
  (let [title (get-in _request [:params "title"])
        date (get-in _request [:params "review_date"])]
    (response (str (db_ops/insert {:title title :review_date date})))))
            ; (with-out-str (clojure.pprint/pprint (get body 0)))))

(defn not-found [_request]
  {:status 404
   :headers {"Content-Type" "text/html"}
   :body (h/html
          (page/html5
           [:head]
           [:body
            [:div "Not found"]]))})
