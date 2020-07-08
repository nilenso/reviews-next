(ns reviews-next.pages.feedback-event
  (:require
   ["@material-ui/core" :as material-ui]
   [reagent.core :as reagent]
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

(def markdown-style
  {:display "flow-root"})

(def review-event-name
  {:display "flex"
   :flex-direction "row"
   :margin-bottom "1em"
   :line-height "21px"
   :font-size "18px"
   :width "100%"
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
   :margin "20px"
   :margin-left "0px"
   :width "20vw"
   :justify-content "flex-start"})

(defn view-review-description
  [review-title review-description]
  (reagent/with-let [open? (reagent/atom false)]
    (js/console.log "Outside Button" @open?)
    [:div
     [components/Button
      {:onClick
               (fn []
                 (reset! open? true)
                 (js/console.log "Inside button " @open?))
       :style {:margin "0px 5px"
               :color "#00947E"}} "View"]
     [components/Dialog
      {:open @open?
       :onClose (fn [] (reset! open? false))}
      {:onClose (fn [] (reset! open? false))}
      review-title
      review-description]]))

(defn publish-button
  []
  (let [current-user @(re-frame/subscribe [::subs/current-user])
        current-review-event @(re-frame/subscribe [::subs/current-review-event])
        feedback @(re-frame/subscribe [::subs/feedback])
        level @(re-frame/subscribe [::subs/level])]
    [components/Button
     {:variant "contained"
      :on-click
               #(re-frame/dispatch [::events/publish-feedback {:from_uid "U3"
                                                                :to_uid (:id current-user)
                                                                :review_id (:id current-review-event)
                                                                :feedback feedback
                                                                :level level}])
      :style {:margin "5px"
              :background "#EEF6FC"
              :color "#257942"} }"Publish"]))

(defn feedback-markdown
  []
  [components/MarkDownEditor
   {:style {:width "100%"
            :height "50vh"
            :margin-block-end "2em"}
    :placeholder "Add Feedback"
    :onChange  #(re-frame/dispatch [::events/feedback-change (. %4 getText)])}])

(defn current-user-component
  [users-for-review]
  (let [current-user @(re-frame/subscribe [::subs/current-user])]
    (js/console.log "Inside Users component" current-user)
    ; (js/console.log users-for-review)
    [:div.feedback-user-display (use-style current-user-name)
     [:h3 "Feedback from You to "]
     [components/Select-Users
                        {:defaultValue "User"
                         :multiple false
                         :style {
                                 :margin-left "30px"
                                 :line-height "49px"
                                 :font-size "36px"}
                         :onChange #(re-frame/dispatch [::events/set-current-user-from-menu (-> % .-target .-value)])}
                        @users-for-review]]))

(defn current-review-event-component
  []
  (let [review-events @(re-frame/subscribe [::subs/review-events])
        current-review-event @(re-frame/subscribe [::subs/current-review-event])
        _ (re-frame/dispatch [::events/get-users-for-review (:id current-review-event)])
        users-for-review (re-frame/subscribe [::subs/users-for-review])]
       [:div

        [current-user-component users-for-review]
        [:div.review-event-name-display (use-style review-event-name)
         [:h3 "For Review Event:"]
         ; (js/console.log "Inside review component" current-review-event)
         [:b {:style {:color "#00947E"}} (:title current-review-event)]
         [view-review-description (:title current-review-event) (:review_description current-review-event)]
         [components/Select-Review-Event
                            {:defaultValue "Change"
                             :multiple false
                             :style {:margin "0px"
                                     :line-height "18px"
                                     :color "#00947E"}
                             :onChange #(re-frame/dispatch [::events/set-current-review-item-from-menu (-> % .-target .-value)])}
                            review-events]]]))
;; main code
(defn feedback-event []
  (let []
   (reagent/create-class
    {:component-did-mount
     (fn []
       (js/console.log "Call Review events")
       (re-frame/dispatch [::events/populate-review-events-list "U1"]))
     :display-name "Main component"
     :reagent-render
     (fn []
       (js/console.log "Main Render")
       [:div.main-content (use-style main-content-style)
          [:div.side-section (use-style (section-style "20vw"))]
          [:div.main-section (use-style (section-style "80vw"))
           ; (current-user-component current-review-event)
           [current-review-event-component]
           [:div#markdown (use-style markdown-style)
            [feedback-markdown]]
           [:div.level (use-style level-style)
            [:label {:for "level"} "Level:"]
            [:input {:type "number"
                     :id "level"
                     :on-change #(re-frame/dispatch [::events/set-level (-> % .-target .-value)])}]]
           ; [:div.Modal
           ;  [components/Button-Modal]]
           [:div.buttons (use-style linear-buttons)
            [components/Button
             {:variant "contained"
              ; :onClick call-save-api
              :style {:margin "5px"
                      :background "#EEF6FC"
                      :color "#1D72AA"}} "Save As Draft"]
            [publish-button]]]])})))
