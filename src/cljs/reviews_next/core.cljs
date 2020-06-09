(ns reviews-next.core
  (:require [reagent.dom :as rdom]
            [re-frame.core :as re-frame]
            [reviews-next.pages.login :as login]
            [reviews-next.pages.review_event :as review_event]))

(defn app []
  [:div
   [:div "Hello App"]
   [review_event/review_event]])

(defn init! []
  (apply (.-log js/console) "Hello World!")
  ; (re-frame/dispatch-sync [:initialize-db])
  ; (re-frame/dispatch-sync [:setup-google-signin-functions])
  (rdom/render [app] (.getElementById js/document "app")))
