(ns reviews-next.events
  (:require [re-frame.core :as re-frame]
            [reviews-next.db :as db]))

(re-frame/reg-event-fx
 ::initialize-db
 (fn [_ _]
   db/initial-db))

(re-frame/reg-event-fx
 ::setup-google-signin-functions
 (fn [_ _]
   (set!
    (.. js/window -onSignIn)
    (clj->js (fn [& args] (apply (.-log js/console) args))))))

(re-frame/reg-event-db
  ::title-change
   (fn [db [_ new-title]]
    (assoc db :review-event-title new-title)))

(re-frame/reg-event-db
  ::date-change
   (fn [db [_ new-date]]
    (assoc db :review-date new-date)))

(re-frame/reg-event-db
  ::description-change
  (fn [db [_ new-date]]
    (assoc db :description new-date)))
