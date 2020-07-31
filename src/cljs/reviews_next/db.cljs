(ns reviews-next.db)

(def initial-db
 {
  :pages
   {:add-review-event
    {:review-event-title ""
     :review-date ""
     :review-description ""
     :all-fields-valid? true
     :participants []
     :selected-participants (vector nil)}
    :add-feedback-event
    {:review-events (vector nil)
     :current-review-event {}
     :users-for-review []
     :current-user {}
     :feedback ""
     :level nil}}})
