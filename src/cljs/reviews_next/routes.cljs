(ns reviews-next.routes
  (:require
   [bidi.bidi :as bidi]
   [reviews-next.db :as db]
   [reviews-next.events :as events]
   [re-frame.core :as re-frame]
   [accountant.core :as accountant]
   [reviews-next.pages.home :as home]))

(def routes
  ["/" {"" [home/home]}])

(accountant/configure-navigation!
 {:nav-handler   (fn [path]
                   (apply (.-log js/console) path)
                   (re-frame/dispatch [::events/set-current-component home/home]))
  :path-exists?  (fn [path] (boolean (bidi/match-route routes path)))
  :reload-same-path? true})
