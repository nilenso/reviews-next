(ns reviews-next.routes
  (:require [bidi.bidi :as b]
            [reviews-next.handlers.pages :as pages]
            [reviews-next.handlers.users :as users]))

(def routes
  ["/" [["" pages/index]
        ["home/" pages/home]
        ["users/" [["login/" {:post users/login}]]]
        [true pages/not-found]]])

(def path-for (partial b/path-for routes))
;; (def routes-old
;;   ["/" {"" pages/index
;;         "api/" {"review-event" {:post {"" (call-api review-event/insert-into-db)}}
;;                 "users" (wrap-json-response review-event/users-list)
;;                 "review-events-list" (wrap-json-response feedback-event/reviews-list)
;;                 "review-for-user"  (wrap-json-response feedback-event/get-reviewer-and-review-event)
;;                 "users-from-review" {:get {"" (wrap-json-response (wrap-params (wrap-keyword-params feedback-event/users-list)))}}
;;                 "feedback-from-id" {:get {"" (wrap-json-response (wrap-params (wrap-keyword-params feedback-event/feeback-details-from-id)))}}
;;                 "publish-feedback" {:post {"" (wrap-json-response (call-api feedback-event/into-user-feedback))}}}
;;         true pages/not-found}])
