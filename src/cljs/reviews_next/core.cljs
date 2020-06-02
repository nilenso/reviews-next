(ns reviews-next.core
  (:require [reagent.dom :as rdom]
            [re-frame.core :as reframe]))

;; (reframe/dispatch-sync [:initialize-db])

(defn on-sign-in [& args]
  (apply (.-log js/console) args))

(defn sign-in []
  [:div {:class "g-signin2"
         :data-onsuccess "onSignIn"}])

(defn app []
  [:div
   [:div "Hello World!"]
   [sign-in]])

(defn init! []
  (set! (.. js/window -onSignIn) (clj->js on-sign-in))
  (rdom/render [app] (.getElementById js/document "app")))
