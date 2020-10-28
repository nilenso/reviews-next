(ns reviews-next.server
  (:require [bidi.ring :as bidi]
            [mount.core :refer [defstate]]
            [reviews-next.config :refer [config]]
            [reviews-next.routes :refer [routes]]
            [reviews-next.middleware :refer [wrap-path-for-fn]]
            [ring.adapter.jetty :as jetty]
            [ring.logger :as logger]
            [ring.middleware.cookies :refer [wrap-cookies]]
            [ring.middleware.json :refer [wrap-json-params]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.not-modified :refer [wrap-not-modified]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.resource :refer [wrap-resource]]
            [taoensso.timbre :as log]))

(def handler
  (-> routes
      bidi/make-handler
      (wrap-path-for-fn routes)
      wrap-cookies
      wrap-keyword-params
      wrap-multipart-params
      wrap-params
      logger/wrap-with-logger
      (wrap-resource "public")
      wrap-not-modified))

(defstate server
  :start (do
           (log/merge-config! (:log config))
           (jetty/run-jetty
             handler
             {:port (-> config :server :port) :join? false}))
  :stop (.stop server))
