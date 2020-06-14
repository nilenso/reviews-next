(ns reviews-next.handlers.post-title-date
  (:require [hiccup.core :as h]
            [hiccup.page :as page]
            [clojure.pprint :as pp]
            [reviews-next.config :as config]))

(defn index [_request]
  (def title (get-in _request [:route-params :title]))
  (def date (get-in _request [:route-params :date]))
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (with-out-str (clojure.pprint/pprint {date,title}))})


(defn not-found [_request]
  {:status 404
   :headers {"Content-Type" "text/html"}
   :body (h/html
          (page/html5
           [:head]
           [:body
            [:div "Not found"]]))})
