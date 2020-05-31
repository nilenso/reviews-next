(ns reviews-next.server
  (:require [bidi.ring :refer [make-handler]]
            [mount.core :refer [defstate]]
            [ring.logger :as logger]
            [ring.adapter.jetty :as jetty]
            [reviews-next.config :refer [config]]
            [reviews-next.routes :refer [routes]]))

(def handler
  (-> routes
      make-handler
      logger/wrap-with-logger))

(defstate server
  :start (jetty/run-jetty
          handler
          {:port (-> config :server :port) :join? false})
  :stop (.stop server))
