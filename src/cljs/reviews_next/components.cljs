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

(defn Table [title head body style]
  [:> material-ui/TableContainer
   {:style style
    :align "center"}  title
   [:> material-ui/Table
    {:size "small"
     :style {:background "white"}}
    [:> material-ui/TableHead
     [:> material-ui/TableRow
      (for [head-col head]
        [:> material-ui/TableCell {:align "center"} head-col])]]
    [:> material-ui/TableBody
     (for [body-row body]
       [:> material-ui/TableRow
        (for [body-row-col body-row]
          [:> material-ui/TableCell
           {:align "center"
            :style {:font-size "12px"
                    :margin "2px"}} body-row-col])])]]])
