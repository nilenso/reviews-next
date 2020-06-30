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
