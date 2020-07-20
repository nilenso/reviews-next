(ns reviews-next.core
  (:require [reagent.dom :as rdom]
            [re-frame.core :as re-frame]
            [reviews-next.pages.login :as login]
            [reviews-next.pages.review-event :as review-event]))

(defn app []
  [:div
   [review-event/review-event]])

(defn init! []
  (re-frame/dispatch [:reviews-next.events/initialize-db])
  (re-frame/dispatch [:reviews-next.events/populate-participants])
  (re-frame/dispatch-sync [:setup-google-signin-functions])
  (rdom/render [app] (.getElementById js/document "app")))