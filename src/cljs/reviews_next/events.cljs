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

(re-frame/reg-event-db
  ::title-change
  (fn [db [_ new-title]]
    (assoc db :review_event_title new-title)))

(re-frame/reg-event-db
  ::date-change
  (fn [db [_ new-date]]
    (assoc db :review_date new-date)))

(re-frame/reg-event-db
  ::level-change
  (fn [db [_ new-date]]
    (assoc db :review_level new-date)))


(re-frame/reg-event-db
  ::description-change
  (fn [db [_ new-date]]
    (assoc db :description new-date)))
