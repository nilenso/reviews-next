(ns reviews-next.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::review-event-title
  (fn [db _]
    (:review-event-title db)))

(re-frame/reg-sub
  ::review-date
  (fn [db _]
    (:review-date db)))

(re-frame/reg-sub
  ::description
  (fn [db _]
    (:description db)))

(re-frame/reg-sub
  ::all-fields-valid?
  (fn [db _]
    (:all-fields-valid? db)))
