(ns reviews-next.db)

(def initial-db
 {:pages {:current-component nil
          :add-review-event {:review-event-title ""
                             :review-date ""
                             :description "DES"
                             :all-fields-valid? true
                             :participants []
                             :selected-participants []}
          :add-feedback-event {:review-events []
                               :current-review-event {}
                               :users-for-review []
                               :current-user {}
                               :feedback ""
                               :level nil}
          :list-feedbacks-provided-page {:feedbacks-list []}
          :draft-feedbacks-list []
          :draft {:draft-review-description ""
                  :current-feedback-to-edit {}
                  :draft-level 0}}})
