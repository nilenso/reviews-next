(ns reviews-next.pages.view-feedback-event
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


(defn publish-button
  []
  (let [current-user @(re-frame/subscribe [::subs/current-user])
        current-review-event @(re-frame/subscribe [::subs/current-review-event])
        feedback @(re-frame/subscribe [::subs/feedback])
        level @(re-frame/subscribe [::subs/level])]
    (components/Button
     {:variant "contained"
      :on-click
      #(re-frame/dispatch [::events/publish-feedback {:from_uid "U3"
                                                      :to_uid (:id current-user)
                                                      :review_id (:id current-review-event)
                                                      :feedback feedback
                                                      :level level}])
      :style {:margin "5px"
              :background "#EEF6FC"
              :color "#257942"}} "Publish")))



;; main code
(defn view-feedback-event []
  (let [user-and-review-list @(re-frame/subscribe [::subs/user-and-review-ids])]
    (reagent/create-class
     {:component-did-mount
      (fn []
        (re-frame/dispatch [::events/populate-user-and-review-ids])
        (js/console.log "userssss " user-and-review-list))
      
      :display-name "Main View Feedback Component"
      :reagent-render
      (fn []
        [:div.main-content (use-style main-content-style)
        ; [:div.side-section (use-style (section-style "20vw"))]
         [:div.main-section (use-style (section-style "80vw"))
          ;; (js/console.log "userssss " user-and-review-list)
          (for [user-and-review user-and-review-list]
            [:div.buttons (use-style linear-buttons)
             (components/Button
              {:variant "contained"
              ; :onClick call-save-api
               :style {:margin "5px"
                       :background "#EEF6FC"
                       :color "#1D72AA"}} (:name (first (:user user-and-review))))
             [:b {:style {:color "#00947E"}} (:title (first (:review user-and-review)))]
             (js/console.log "USR" (:title (first (:review user-and-review))))])
          ]])})))
