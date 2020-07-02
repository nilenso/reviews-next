(ns reviews-next.components
  (:require
   ["react-quill" :as ReactQuill]
   ["@material-ui/core" :as material-ui]))

(defn TextField
  [component-value]
  [:> material-ui/TextField component-value])

(defn Button
  [component-value text]
  [:> material-ui/Button component-value text])

(defn MarkDownEditor [component-value]
  [:> ReactQuill component-value])

(defn Select
  ([]
   [:> material-ui/Select {:value {:age ["10"]}
                           :onChange (js/console.log "Clicked")}
    [:> material-ui/Option {:value "10"} "Ten"]]))
    ; (for [option options]
    ;   [:> material-ui/Option {:value (:value option)}])]))
