(ns reviews-next.routes
  (:require [bidi.ring :as bidi]
            [ring.adapter.jetty :as jetty]
            [clojure.pprint :as pp]
            [reviews-next.handlers.pages :as pages]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.json :refer [wrap-json-params wrap-json-body wrap-json-response]]
            [reviews-next.handlers.review-event :as review-event]))

(def call-api (partial wrap-json-params))

(def routes
  ["/" {
        "" pages/index
        "api/" {
                "review-event" {:post {"" (call-api review-event/insert-into-db)}}
                "users" (wrap-json-response review-event/users-list)}
        "assets" (bidi/resources {:prefix "assets/"})
        true pages/not-found}])

; (def handler
;   (wrap-cors routes :access-control-allow-origin [#"http://localhost:3000/"]
;                     :access-control-allow-methods [:get :put :post :delete]))
