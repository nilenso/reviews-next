(ns reviews-next.components
  (:require
   ["@material-ui/core" :as material-ui]))

(defn TextField
  [component-value]
  [:> material-ui/TextField component-value])

(defn Button
  [component-value text]
  [:> material-ui/Button component-value text])
