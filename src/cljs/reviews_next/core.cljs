(ns reviews-next.core
  (:require [reagent.dom :as rdom]
            [re-frame.core :as reframe]))

(reframe/dispatch-sync [:initialized-db])

(defn app []
  [:div "Hello World!"])

(defn init! []
  (rdom/render [app] (.getElementById js/document "container")))
