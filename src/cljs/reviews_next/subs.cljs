(ns reviews-next.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::review_event_title
  (fn [db _]
    (:review_event_title db)))

(re-frame/reg-sub
  ::review_date
  (fn [db _]
    (:review_date db)))

(re-frame/reg-sub
  ::review_level
  (fn [db _]
    (:review_level db)))

(re-frame/reg-sub
  ::description
  (fn [db _]
    (:description db)))
