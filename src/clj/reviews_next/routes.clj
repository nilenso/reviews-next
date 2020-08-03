(ns reviews-next.routes
  (:require [bidi.ring :as bidi]
            [ring.adapter.jetty :as jetty]
            [clojure.pprint :as pp]
            [reviews-next.handlers.pages :as pages]
            [reviews-next.handlers.auth :as auth]
            [reviews-next.handlers.review-event :as review-event]
            [reviews-next.handlers.feedback-event :as feedback-event]))

(def routes
  ["/" {"" pages/index
        "api/" {:post {"google/login" auth/login}
                "review-event" {:post {"" review-event/insert-into-db}}
                "users" review-event/users-list
                "review-events-list" feedback-event/reviews-list
                "review-for-user"  feedback-event/get-reviewer-and-review-event
                "users-from-review" {:get {"" feedback-event/users-list}}
                "feedback-from-id" {:get {"" feedback-event/feeback-details-from-id}}
                "publish-feedback" {:post {"" feedback-event/into-user-feedback}}}
        "assets" (bidi/resources {:prefix "assets/"})
        true pages/not-found}])
