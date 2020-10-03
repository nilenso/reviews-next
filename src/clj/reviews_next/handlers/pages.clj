(ns reviews-next.handlers.pages
  (:require [reviews-next.handlers.core :as handlers]))


(defn index [_request]
  (handlers/page
    {:body
     [:div#app
      [:div.login
       [:img {:src "/images/vertical.svg"}]
       [:div
        {:class "g-signin2"
         :data-width "240"
         :data-heigth "50"
         :data-theme "dark"
         :data-longtitle "240"
         :data-onsuccess "onSignIn"}]]]}))

(defn not-found [_request]
  (handlers/page
    {:status 404
     :body [:div "Not found"]}))
