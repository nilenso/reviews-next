(ns reviews-next.db)

(def initial-db
 {
  :pages
   {:current-component nil
    :add-review-event
    {:review-event-title ""
     :review-date ""
     :description "DES"
     :all-fields-valid? true
     :participants []
     :selected-participants (vector nil)}
    :add-feedback-event
    {:review-events (vector nil)
     :current-review-event {}
     :users-for-review []
     :current-user {}
     :feedback ""
     :level nil}
    :view-feedback-event
    {:user-and-review-ids (vector nil)
     :feedback-details {}
     }}})