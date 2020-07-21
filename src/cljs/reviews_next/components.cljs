(ns reviews-next.components
  (:require
   [reagent.core :as reagent]
   ["react-quill" :as ReactQuill]
   ["@material-ui/core" :as material-ui]
   [accountant.core :as accountant]
   [secretary.core :as secretary]))

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


(defn Customisable-button
  [data review-id]
  (Button
    {
     :on-click (fn [] (accountant/navigate! (str "/view-feedback/" review-id)))
     :style {:background "transparent"
             :border "none"
             ;:background "white"
             :color "#257942"}} data))

(defn Table [title head body style]
  [:> material-ui/TableContainer
   {:style style
    :align "center"}  title
   [:> material-ui/Table
    {:size "small"
     :style {:background "white"}}
    [:> material-ui/TableHead
     [:> material-ui/TableRow
      [:> material-ui/TableCell {:align "left"} (first head)]
      [:> material-ui/TableCell {:align "right"} (second head)]
      ]]
    [:> material-ui/TableBody
     (for [body-row body]
       [:> material-ui/TableRow
        [:> material-ui/TableCell
         {:align "left"
          :style {:font-size "12px"
                  :margin "2px"}} (Customisable-button (first body-row) (last body-row))]
        [:> material-ui/TableCell
         {:align "right"
          :style {:font-size "12px"}} (second body-row)]
        ])]]])
