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
   [accountant.core :as accountant]
   [reviews-next.components :as components]
   [secretary.core :as secretary])
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
  ; :margin "10px"
   :margin-left "0px"
   :line-height "21px"
   :font-size "18px"
   :width "55vw"
   :justify-content "space-between"})

(def feedback-box-title
  {:display "flex"
   :flex-direction "row"
   :margin "10px 0px 10px 0px"
   :line-height "49px"
   :font-size "24px"
   :width "55vw"
   :justify-content "space-between"})

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

(def feedback-table-title-style
  {:margin "10px"
   :width "55vw"
   :margin-left "0px"
   :line-height "35px"
   :border "1px solid #FEFEFE"
   :box-shadow "0px 4px 4px rgba(0, 0, 0, 0.25"
   :border-radius "4px"
   :background "#EFFAF3"
   :color "#1D72AA"
   :font-family "Noto Sans"
   :font-style "normal"
   :font-weight "normal"
   :font-size "20px"})

(def feedback-description-box
  {:font-family "Noto Sans"
   :font-style "normal"
   :font-weight "normal"
   :font-size "12px"
   :line-height "25px"
   :height "25vw"
   :margin "10px 0px"
   :border "1px solid #FEFEFE"
   :box-shadow "0px 4px 4px rgba(0, 0, 0, 0.25"
   :color "#000000"})



(defn view-feedback-page [review-id]
  (let [feedback-details (re-frame/subscribe [::subs/feedback-details])
        reviewer-and-review-event (re-frame/subscribe [::subs/current-reviewer-and-review-event]) ]
    (reagent/create-class
     {:constructor
      (fn []
        (re-frame/dispatch [::events/populate-feedback-from-review-id review-id]))
      :display-name "Display Feedback Component"
      :reagent-render
      (fn []
        ;; (js/console.log "Feedback: " (first @feedback-details))
        [:div.main-content (use-style main-content-style)
         [:div.side-section (use-style (section-style "20vw"))]
         [:div.main-section (use-style (section-style "80vw"))
          [:div.feedback-user-display (use-style feedback-box-title)
           [:h3 "Feedback from " (:name (first (:reviewer @reviewer-and-review-event))) " to You"]
           [:h3 "Level: " (:level (first @feedback-details))]]
          ;; [:div.review-event-name-display (use-style review-event-name)
           [:h3 "For Review Event: " (:title (first (:review-event @reviewer-and-review-event)))]
          ;; ]
          [:p (use-style feedback-description-box) (:feedback (first @feedback-details))]
          ;; [:p "hiiiiiiiiiiii"]
          ;[:p review-id]
          ]])})))




(defn join-user-review [user-and-review]
  [(:name (first (:reviewer user-and-review)))
   (:title (first (:review-event user-and-review)))
   (:id user-and-review)])

;; main code
(defn view-feedback-event []
  (let [user-and-review-list (re-frame/subscribe [::subs/user-and-review-ids])]
    (reagent/create-class
     {:constructor
      (fn []
        (re-frame/dispatch [::events/populate-user-and-review-ids])
        (js/console.log "userssss " @user-and-review-list))

      :display-name "Main View Feedback Component"
      :reagent-render
      (fn []
        [:div.main-content (use-style main-content-style)
         [:div.side-section (use-style (section-style "20vw"))]
         [:div.main-section (use-style (section-style "80vw"))
          (components/Table "Reviews" ["Reviewer" "Review Event"] 
                            (map join-user-review @user-and-review-list) feedback-table-title-style)
          (re-frame/dispatch [::events/set-current-reviewer-and-review-event (first @user-and-review-list)])
          ]])
      })))
