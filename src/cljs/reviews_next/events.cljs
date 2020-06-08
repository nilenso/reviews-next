(ns reviews-next.events
  (:require [re-frame.core :as re-frame]
            [reviews-next.db :as db]))

(re-frame/reg-event-fx
 ::initialize-db
 (fn [_ _]
   {:db db/initial-db}))

(re-frame/reg-event-fx
 ::setup-google-signin-functions
 (fn [_ _]
   (set!
    (.. js/window -onSignIn)
    (clj->js (fn [& args] (apply (.-log js/console) args))))
   {}))
