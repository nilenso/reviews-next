(ns reviews-next.server
  (:require [bidi.ring :as bidi]
            [mount.core :refer [defstate]]
            [ring.adapter.jetty :as jetty]
            [reviews-next.config :refer [config]]
            [reviews-next.routes :refer [routes]]
            [ring.logger :as logger]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.not-modified :refer [wrap-not-modified]]))

(def handler
  (-> routes
      bidi/make-handler
      wrap-keyword-params
      wrap-params
      logger/wrap-with-logger
      (wrap-resource "public")
      wrap-not-modified))

(defstate server
  :start (jetty/run-jetty
          handler
          {:port (-> config :server :port) :join? false})
  :stop (.stop server))
