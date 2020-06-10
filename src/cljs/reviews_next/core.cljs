(ns reviews-next.core
  (:require [reagent.dom :as rdom]
            [re-frame.core :as re-frame]
            [reviews-next.pages.login :as login]
            [reviews-next.pages.reviewEvent :as reviewEvent]))
            ; [reviews-next.pages.review :as review]))

(defn app []
  [:div
   ; [:div "Hello App"]
   [reviewEvent/review_event]])

(defn init! []
  (apply (.-log js/console) "Hello World!")
  (re-frame/dispatch-sync [:initialize-db])
  (re-frame/dispatch-sync [:setup-google-signin-functions])
  (rdom/render [app] (.getElementById js/document "app")))
