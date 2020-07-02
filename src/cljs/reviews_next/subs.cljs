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

(re-frame/reg-sub
  ::participants
  (fn [db _]
    (:participants db)))

(re-frame/reg-sub
  ::selected-participants
  (fn [db _]
    (:selected-participants db)))

(re-frame/reg-sub
  ::review-events
  (fn [db _]
    (:review-events db)))

(re-frame/reg-sub
  ::current-review-event
  (fn [db _]
    (:current-review-event db)))
