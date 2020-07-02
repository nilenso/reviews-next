(ns reviews-next.pages.feedback-event
  (:require
   ["@material-ui/core" :as material-ui]
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
   :width "35vw"
   :justify-content "space-between"})

;; init functions
(defn list-review-events
  []
  (go (let [response (<! (http/get "/api/review-events-list"))
            review-events (re-frame/subscribe [::subs/review-events])]
        (re-frame/dispatch [::events/set-review-events (:body response)])
        (re-frame/dispatch [::events/set-current-review-event (first @review-events)]))))

;;npm components
(defn Select
  []
  [:> material-ui/Menu
   [:> material-ui/MenuItem {:onClick (js/console.log "Clciked") } "Profile"]])


;;components
(defn log-review-events
  [review-events]
  (for [review-event review-events]
    (js/console.log (:title review-event))))

;; main code
(defn feedback-event []
  (let [review-events (re-frame/subscribe [::subs/review-events])
        current-review-event @(re-frame/subscribe [::subs/current-review-event])]
   (list-review-events)
   [:div.main-content (use-style main-content-style)
      [:div.side-section (use-style (section-style "20vw"))]
      [:div.main-section (use-style (section-style "80vw"))
       [:div.review-event-name-display (use-style review-event-name)
        [:h3 "For Review Event:"]
        [:b {:style {:color "#00947E"}} (:title current-review-event)]]
       [:div#box-button (use-style box-button-style)
        ; (js/console.log "List" @review-events)
        (components/MarkDownEditor
         {:style {:width "80%"
                  :height "50vh"
                  :padding-bottom "20px"}
          :placeholder "Add description"
          :onChange  #(re-frame/dispatch [::events/description-change (. %4 getText)])})]
       ; (components/Select)
       (log-review-events @review-events)
       (components/Button
        {:variant "contained"
         ; :onClick call-save-api
         :style {:margin "5px"
                 :background "#f8337d"
                 :color "white"}} "Save")]]))
