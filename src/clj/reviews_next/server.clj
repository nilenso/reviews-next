(ns reviews-next.server
  (:require [bidi.ring :refer [make-handler]]
            [mount.core :refer [defstate]]
            [ring.logger :refer [wrap-with-logger]]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.json :refer [wrap-json-params wrap-json-body wrap-json-response]]
            [ring.adapter.jetty :as jetty]
            [reviews-next.config :refer [config]]
            [reviews-next.routes :refer [routes]]))

(def handler
  (-> routes
      make-handler
      wrap-json-body
      wrap-params
      wrap-keyword-params
      wrap-json-params
      wrap-json-response
      wrap-with-logger))

(defstate server
  :start (jetty/run-jetty
          handler
          {:port (-> config :server :port) :join? false})
  :stop (.stop server))
