(ns reviews-next.handlers.pages
  (:require [reviews-next.views.home :as home]
            [reviews-next.views.not-found :as not-found]
            [reviews-next.handlers.core :as handlers]
            [reviews-next.views.index :as index]))


(defn index [_request]
  (handlers/page {:body (index/page)}))

(defn home [_]
  (handlers/logged-in-page
    {:body (home/page)}))

(defn not-found [_request]
  (handlers/page
    {:status 404
     :body (not-found/page)}))

(defn oops [_]
  (handlers/page
    {:body
     [:section {:class "section"}
      [:div {:class "container"}
       [:h1 {:class "is-size-1"} "Something went wrong"]]]}))
