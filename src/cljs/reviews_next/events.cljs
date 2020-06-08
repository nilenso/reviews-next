(ns reviews-next.events
  (:require [re-frame.core :as re-frame]
            [reviews-next.db :as db]))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   {:db (db/initial-db)}))

(re-frame/reg-fx
 ::setup-google-signin-functions
 (fn []
   (set!
    (.. js/window -onSignIn)
    (cljs->js (fn [& args] (apply (.-log js/console) args))))))
