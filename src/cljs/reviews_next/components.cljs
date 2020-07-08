(ns reviews-next.components
  (:require
   [reagent.core :as reagent]
   ["react-quill" :as ReactQuill]
   ["@material-ui/core" :as material-ui]))

(defn TextField
  [props-map]
  [:> material-ui/TextField props-map])

(defn Button
  [props-map text]
  [:> material-ui/Button props-map text])

(defn MarkDownEditor [props-map]
  [:> ReactQuill props-map])

(defn Select-Review-Event
  ([select-props-map review-events-list]
   [:> material-ui/Select select-props-map
    (for [review-event review-events-list]
      ^{:key (:id review-event)}
      [:> material-ui/MenuItem {:value (:id review-event)} (:title review-event)])]))

(defn Select-Users
  ([select-props-map users-list]
   [:> material-ui/Select select-props-map
    (for [user users-list]
      ^{:key (:id user)}
      [:> material-ui/MenuItem {:value (:id user)} (:name user)])]))

(defn DialogTitle
  [dialog-title-props title]
  [:> material-ui/DialogTitle dialog-title-props title])

(defn DialogContent
  [content]
  [:> material-ui/DialogContent content])

(defn Dialog
  [dialog-props dialog-title-props title content]
  [:> material-ui/Dialog dialog-props
   [DialogTitle dialog-title-props title]
   [DialogContent content]])
