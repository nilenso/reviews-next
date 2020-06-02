(ns reviews-next.events
  (:require [re-frame.core :as re-frame]
            [reviews-next.db :as db]))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   {:db (db/initial-db)}))
