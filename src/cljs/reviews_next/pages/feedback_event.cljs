(ns reviews-next.pages.feedback-event
  (:require
   ["@material-ui/core" :as material-ui]
   ; [cljsjs.material-ui]
   ; [cljs-react-material-ui.core :refer [get-mui-theme color]]
   ; [cljs-react-material-ui.reagent :as ui]
   [re-frame.core :as re-frame]
   [cljs.core.async :refer [<!]]
   [cljs-http.client :as http]
   [clojure.pprint :as pp]
   [cljs.spec.alpha :as s]
   [stylefy.core :as stylefy :refer [use-style]]
   [reviews-next.db :as db]
   [reviews-next.events :as events]
   [reviews-next.subs :as subs]
   [reviews-next.components :as components])
  (:require-macros
    [cljs.core.async.macros :refer [go]]))

;;styles
(def main-content-style
  {:display "flex"
   :flex-direction "row"})

(defn section-style [width]
  {:margin "40px"
   :width width})

(def box-button-style
  {:display "flex"
   :flex-direction "row"
   :justify-content "space-between"
   :text-align "center"})

(def checkbox-area
  {:display "flex"
   :margin "20px 0px 20px 0px"
   :margin-left "25vw"
   :margin-right "25vw"
   :height "40vh"
   :flex-direction "column"
   :flex-wrap "wrap"
   :aligh-content "space-between"})

(def checkbox-style
  {:margin "2%"})

(def review-event-name
  {:display "flex"
   :flex-direction "row"
   :margin "10px"
   :margin-left "0px"
   :line-height "21px"
   :font-size "18px"
   :width "55vw"
   :justify-content "space-between"})

(def current-user-name
  {:display "flex"
   :flex-direction "row"
   :margin "30px 0px 30px 0px"
   :line-height "49px"
   :font-size "36px"
   :width "55vw"
   :justify-content "flex-start"})

(def linear-buttons
  {:display "flex"
   :flex-direction "row"
   :left "20vw"
   :margin "20px"
   :margin-left "0px"
   :width "40vw"
   :justify-content "flex-start"})

(def level-style
  {:display "flex"
   :flex-direction "row"
   :left "20vw"
   :margin "20px"
   :margin-left "0px"
   :width "40vw"
   :justify-content "flex-start"})

(defn get-current-review-event
  [review-events]
  (first review-events))

(defn current-feedback-for-user-component
  [current-review-id]
  (let [_ (re-frame/dispatch [::events/get-users-for-review current-review-id])
        current-user @(re-frame/subscribe [::subs/current-user])
        users-for-review @(re-frame/subscribe [::subs/users-for-review])]
    (js/console.log users-for-review)
    [:div.feedback-user-display (use-style current-user-name)
     [:h3 "Feedback from You to "]
     (components/Select-Users
                        {:defaultValue "User"
                         :multiple false
                         :style {
                                 :margin-left "30px"
                                 :line-height "49px"
                                 :font-size "36px"}
                         :onChange #(re-frame/dispatch [::events/set-current-user-from-menu (-> % .-target .-value)])}
                        users-for-review)]))

(defn current-review-event-component
  [review-events]
  (let [current-review-event @(re-frame/subscribe [::subs/current-review-event])]
    [:div
     (current-feedback-for-user-component (:id current-review-event))
     [:div.review-event-name-display (use-style review-event-name)
      [:h3 "For Review Event:"]
      [:b {:style {:color "#00947E"}} (:title current-review-event)]
      (components/Button
       {; :onClick call-save-api
        :style {:margin "0px 5px"
                :color "#00947E"}} "View")
      (components/Select-Review-Event
                         {:defaultValue "Change"
                          :multiple false
                          :style {:margin "0px"
                                  :line-height "18px"
                                  :color "#00947E"}
                          :onChange #(re-frame/dispatch [::events/set-current-review-item-from-menu (-> % .-target .-value)])}
                         review-events)]]))
;; main code
(defn feedback-event []
  (let [review-events @(re-frame/subscribe [::subs/review-events])
        current-review-event (get-current-review-event review-events)]
   (re-frame/dispatch [::events/populate-review-events-list])
   (fn []
     [:div.main-content (use-style main-content-style)
        [:div.side-section (use-style (section-style "20vw"))]
        [:div.main-section (use-style (section-style "80vw"))


         (current-review-event-component review-events)
         [:div#box-button (use-style box-button-style)
          (components/MarkDownEditor
           {:style {:width "80%"
                    :height "50vh"
                    :padding "20px"
                    :margin "10px 0px 10px 0px"}
            :placeholder "Add Feedback"
            :onChange  #(re-frame/dispatch [::events/description-change (. %4 getText)])})]

         [:div.level (use-style level-style)
          [:label {:for "level"} "Level:"]
          [:input {:type "number"
                   :id "level"
                   :on-change #(re-frame/dispatch [::events/set-level (-> % .-target .-value)])}]]
         [:div.buttons (use-style linear-buttons)
          (components/Button
           {:variant "contained"
            ; :onClick call-save-api
            :style {:margin "5px"
                    :background "#EEF6FC"
                    :color "#1D72AA"}} "Save As Draft")
          (components/Button
           {:variant "contained"
            ; :onClick call-save-api
            :style {:margin "5px"
                    :background "#EEF6FC"
                    :color "#257942"}} "Publish")]]])))
