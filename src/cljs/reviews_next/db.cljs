(ns reviews-next.db)

(def initial-db
 {
  :pages
   {:add-review-event
    {:review-event-title ""
     :review-date ""
     :description ""
     :all-fields-valid? true
     :participants []
     :selected-participants (vector nil)}}})
