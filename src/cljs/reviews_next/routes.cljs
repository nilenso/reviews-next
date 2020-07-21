(ns reviews-next.routes
  (:require
   [reviews-next.db :as db]
   [reviews-next.events :as events]
   [re-frame.core :as re-frame]
   [accountant.core :as accountant]
   [secretary.core :as secretary]
   [reviews-next.pages.list-feedbacks-provided :as list-feedbacks-provided]))

(accountant/configure-navigation!
 {:nav-handler   (fn [path]
                   (re-frame/dispatch [::events/set-current-component (secretary/dispatch! path)]))
  :path-exists?  (fn [path] (secretary/locate-route path))
  :reload-same-path? true})

(secretary/defroute "/view-feedback/:id" {id :id}
  (js/console.log "Okay atleast something" id)
 ; [view-feedback-event/view-feedback-page id]
        )

(secretary/defroute "/" []
  (js/console.log "Okay atleast something")
  [list-feedbacks-provided/start-component])