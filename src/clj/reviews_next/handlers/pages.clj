(ns reviews-next.handlers.pages
  (:require [reviews-next.views.index :as index]
            [reviews-next.views.home :as home]
            [reviews-next.views.not-found :as not-found]
            [reviews-next.handlers.core :as handlers]))


(defn index [_request]
  (handlers/page {:body (index/page)}))

(defn home [_]
  (handlers/page
    {:body (home/page)}))

(defn not-found [_request]
  (handlers/page
    {:status 404
     :body (not-found/page)}))
