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


(accountant/configure-navigation!
 {:nav-handler   (fn [path] (secretary/dispatch! path))
  :path-exists?  (fn [path] (secretary/locate-route path))
  :reload-same-path? true})

(secretary/defroute "/view-feedback" {}
  (js/console.log "Okay atleast something")
)

(defn publish-button
  []
  (let [current-user @(re-frame/subscribe [::subs/current-user])
        current-review-event @(re-frame/subscribe [::subs/current-review-event])
        feedback @(re-frame/subscribe [::subs/feedback])
        level @(re-frame/subscribe [::subs/level])]
    (components/Button
     {:variant "contained"
      :on-click (fn [] (accountant/navigate! "/view-feedback"))

      :style {:margin "5px"
              :background "#EEF6FC"
              :color "#257942"}} "Publish")))


(defn join-user-review [user-and-review]
  [(:name (first (:user user-and-review))) (:title (first (:review user-and-review)))])

(defn view-feedback-page [user-and-review]
  (let [feedback-details @(re-frame/subscribe [::subs/feedback-details])
        review-id (:id (first (:review user-and-review)))]
    (reagent/create-class
     {:component-did-mount
      (fn []
        (re-frame/dispatch [::events/populate-feedback-from-review-id review-id])
        

      :display-name "Display Feedback Component"
      :reagent-render
      (fn []
        (js/console.log "Feedback: " feedback-details))
        [:div.main-content (use-style main-content-style)
         [:div.side-section (use-style (section-style "20vw"))]
         [:div.main-section (use-style (section-style "80vw"))
          [:div.feedback-user-display (use-style current-user-name)
           [:h3 "Feedback from" (:name (first (:user user-and-review))) "to You"]]
          [:div.review-event-name-display (use-style review-event-name)
           [:h3 "For Review Event: " (:title (first (:review user-and-review)))]]
          [:p (:feedback feedback-details)]
          ]]
        )})))


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
         [:div.side-section (use-style (section-style "20vw"))]
         [:div.main-section (use-style (section-style "80vw"))
          ;; [:div.table-section (use-style (table-style))
          (components/Table "Reviews" ["Name" "Review Title"] (map join-user-review user-and-review-list))
          ;;  ]
          ;; (publish-button)
          (for [user-and-review user-and-review-list]
            [view-feedback-page user-and-review])
          ]
         ]
        )
      })))
