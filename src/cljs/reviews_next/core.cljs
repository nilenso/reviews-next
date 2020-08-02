(ns reviews-next.core
  (:require [reagent.dom :as rdom]
            [re-frame.core :as re-frame]
            [reviews-next.pages.login :as login]
            [reviews-next.pages.review-event :as review-event]
            [reviews-next.pages.feedback-event :as feedback-event]
            [reviews-next.pages.view-feedback-event :as view-feedback-event]
            [reviews-next.subs :as subs]
            [accountant.core :as accountant]
            [reviews-next.components :as components]
            [reviews-next.routes :as routes]))

(defn app []
  (let [root-page (re-frame/subscribe ::subs/current-component)])
  [:div])

(defn init! []
  (re-frame/dispatch [:reviews-next.events/initialize-db])
  (accountant/navigate! "/")

  (re-frame/dispatch [:reviews-next.events/populate-participants])
  (re-frame/dispatch-sync [:setup-google-signin-functions])
  (rdom/render [app] (.getElementById js/document "app")))
