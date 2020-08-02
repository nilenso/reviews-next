(ns reviews-next.routes
  (:require
   [bidi.bidi :as bidi]
   [reviews-next.db :as db]
   [reviews-next.events :as events]
   [re-frame.core :as re-frame]
   [accountant.core :as accountant]
   [reviews-next.pages.home :as home]))

(def routes
  ["/" {"" #'home/home}])

(accountant/configure-navigation!
 {:nav-handler (fn [path]
                 (let [active-panel (:handler (bidi/match-route routes path))]
                   (re-frame/dispatch [::events/set-active-panel active-panel])))
  :path-exists? (fn [path] (boolean (bidi/match-route routes path)))
  :reload-same-path? true})
