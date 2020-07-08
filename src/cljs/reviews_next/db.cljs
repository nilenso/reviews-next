(ns reviews-next.db)

(def initial-db
 {
  :pages
   {:add-review-event
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
<<<<<<< HEAD
    {:user-and-review-ids (vector nil)
=======
    {:reviwers-list (vector nil)
>>>>>>> 1020697776d7bca5799bf91041534e3032cb3681
     }}})
