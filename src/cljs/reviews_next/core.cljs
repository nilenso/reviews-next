(ns reviews-next.core
  (:require [reagent.dom :as rdom]
            [re-frame.core :as re-frame]
            [reviews-next.events :as events]
            [reviews-next.pages.login :as login]))

(defn app []
  [login/login])

(defn init! []
  (re-frame/dispatch [::events/initialize-db])
  (re-frame/dispatch-sync [::events/setup-google-signin-functions])
  (rdom/render [app] (.getElementById js/document "app")))
