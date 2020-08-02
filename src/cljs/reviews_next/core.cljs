(ns reviews-next.core
  (:require [reagent.dom :as rdom]
            [re-frame.core :as re-frame]
            [reviews-next.pages.base :as base]
            [reviews-next.pages.login :as login]
            [reviews-next.subs :as subs]
            [reviews-next.events :as events]
            [reviews-next.events.user :as user-events]
            [accountant.core :as accountant]
            [reviews-next.routes :as routes]))

(defn app []
  (let [panel (re-frame/subscribe [::subs/active-panel])
        user (re-frame/subscribe [::subs/user])]
    (if (not (nil? @user))
      (if (fn? @panel)
        [base/base @user @panel]
        [:div.container])
      (login/login))))

(defn init! []
  (re-frame/dispatch-sync [::user-events/setup-google-signin-functions])
  (re-frame/dispatch [::events/initialize-db])

  (accountant/navigate! "/")
  (rdom/render [app] (.getElementById js/document "app")))
