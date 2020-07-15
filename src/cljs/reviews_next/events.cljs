(ns reviews-next.events
  (:require
   [day8.re-frame.http-fx]
   [clojure.set]
   [ajax.core :as ajax]
   [re-frame.core :as re-frame]
   [reviews-next.db :as db]))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   {:db db/initial-db}))

(re-frame/reg-event-db
 ::participants-from-backend
 (fn [db [_ participants-response]]
   (assoc db :participants (vec participants-response))))

(re-frame/reg-event-fx
 ::populate-participants
 (fn [_ _]
   {:http-xhrio {:method :get
                 :uri    "/api/users"
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [::participants-from-backend]
                 :on-fail    [::api-failed]}}))

(re-frame/reg-fx
 ::setup-google-signin-functions
 (fn []
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
  (fn [db [_ new-desc]]
    (assoc db :review-description new-desc)))

(re-frame/reg-event-db
  ::all-fields-valid-change
  (fn [db [_ new-val]]
    (assoc db :all-fields-valid? new-val)))

(re-frame/reg-event-db
  ::set-participants
  (fn [db [_ new-val]]
    (assoc db :participants new-val)))

(re-frame/reg-event-db
  ::add-to-selected-participants
  (fn [db [_ new-val]]
    (assoc db :selected-participants (set (conj (get db :selected-participants) new-val)))))

(re-frame/reg-event-db
  ::remove-from-selected-participants
  (fn [db [_ new-val]]
    (assoc db :selected-participants (disj (set (get db :selected-participants)) new-val))))

(re-frame/reg-event-db
 ::remove-all-selected-participants
 (fn [db [_]]
   (assoc db :selected-participants [])))

(re-frame/reg-event-db
 ::clear-all-fields
 (fn [db [_ new_val]]
   (assoc db :clear-all-fields new_val)))

(re-frame/reg-event-db
  ::set-review-events
  (fn [db [_ new-val]]
    (let [db (assoc db :review-events new-val)]
      (assoc db :current-review-event (first new-val)))))

(re-frame/reg-event-db
  ::set-current-review-item-from-menu
  (fn [db [_ new-val]]
    (let [review-events-set (set (get-in db [:review-events]))
          maps-with-id (clojure.set/index review-events-set [:id])]
      (assoc db :current-review-event (first (get maps-with-id {:id new-val}))))))

(re-frame/reg-event-fx
 ::populate-review-events-list
 (fn [_ _]
   {:http-xhrio {:method :get
                 :uri    "/api/review-events-list"
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [::set-review-events]
                 :on-fail    [::api-failed]}}))

(re-frame/reg-event-db
  ::set-users-for-review
  (fn [db [_ new-val]]
    (assoc db :users-for-review new-val)))

(re-frame/reg-event-fx
 ::get-users-for-review
 (fn [_ [_ current-review-id]]
   {:http-xhrio {:method :get
                 :uri    "/api/users-from-review"
                 :params {:review_id current-review-id}
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [::set-users-for-review]
                 :on-fail    [::api-failed]}}))

(re-frame/reg-event-db
  ::set-current-user-from-menu
  (fn [db [_ new-val]]
    (let [users-for-review-set (set (get-in db [:users-for-review]))
          maps-with-id (clojure.set/index users-for-review-set [:id])]
      (assoc db :current-user (first (get maps-with-id {:id new-val}))))))

(re-frame/reg-event-db
  ::set-level
  (fn [db [_ new-val]]
    (assoc db :level new-val)))

(re-frame/reg-event-fx
 ::publish-feedback
 (fn [_ [_ feedback-param]]
   {:http-xhrio {:method :post
                 :uri    "/api/publish-feedback"
                 :params feedback-param
                 :format          (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [::set-users-for-review]
                 :on-fail    [::api-failed]}}))

(re-frame/reg-event-db
 ::user-and-review-ids
 (fn [db [_ new-val]]
   (assoc db :user-and-review-ids new-val)))

(re-frame/reg-event-fx
 ::populate-user-and-review-ids
 (fn [_ _]
   {:http-xhrio {:method :get
                 :uri    "/api/user-and-review"
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [::user-and-review-ids]
                 :on-fail    [::api-failed]}}))


(re-frame/reg-event-db
 ::set-feedback-details
 (fn [db [_ new-val]]
   (assoc db :feedback-details new-val)))

(re-frame/reg-event-fx
 ::populate-feedback-from-review-id
 (fn [_ [_ review-id]]
   {:http-xhrio {:method :get
                 :uri    "/api/feedback-from-id"
                 :params {:review_id review-id}
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [::set-feedback-details]
                 :on-fail    [::api-failed]}}))

(re-frame/reg-event-db
  ::feedback-change
  (fn [db [_ new-desc]]
    (assoc db :feedback new-desc)))

(re-frame/reg-event-db
 ::set-current-component
 (fn [db [_ new-component]]
   (assoc db :current-component new-component)))
