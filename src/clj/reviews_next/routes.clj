(ns reviews-next.routes
  (:require [bidi.ring :as bidi]
            [ring.adapter.jetty :as jetty]
            [clojure.pprint :as pp]
            [reviews-next.handlers.pages :as pages]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.json :refer [wrap-json-params wrap-json-body wrap-json-response]]
            [reviews-next.handlers.review-event :as review-event]
            [reviews-next.handlers.feedback-event :as feedback-event]))

(def call-api (partial wrap-json-params))

(def routes
  ["/" {
        "" pages/index
        "api/" {
                "review-event" {:post {"" (call-api review-event/insert-into-db)}}
                "users" (wrap-json-response review-event/users-list)
                "review-events-list" (wrap-json-response feedback-event/reviews-list)
                "users-from-review" {:get {"" (wrap-json-response (wrap-params (wrap-keyword-params feedback-event/users-list)))}}}
                ; ["users-from-review" :review_id] (wrap-json-response feedback-event/users-list)}
        "assets" (bidi/resources {:prefix "assets/"})
        true pages/not-found}])

; (def handler
;   (wrap-cors routes :access-control-allow-origin [#"http://localhost:3000/"]
;                     :access-control-allow-methods [:get :put :post :delete]))
