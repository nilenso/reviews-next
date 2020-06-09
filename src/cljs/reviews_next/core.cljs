(ns reviews-next.core
  (:require [reagent.dom :as rdom]
            [re-frame.core :as re-frame]
            [reviews-next.pages.login :as login]
            [reviews-next.pages.review_event :as review_event]))

(defn app []
  [:div
   [review_event/review_event]])

(defn init! []
  (re-frame/dispatch [::events/initialize-db])
  (re-frame/dispatch-sync [::events/setup-google-signin-functions])
  (rdom/render [app] (.getElementById js/document "app")))
