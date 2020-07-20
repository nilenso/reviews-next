(ns reviews-next.pages.review-event
  (:require
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

;;specs
(s/def ::title (s/and #(not (nil? %)) #(<= 1 (count %) 50)))
(s/def ::date (s/and #(not (nil? %)) #(not (empty? %))))
(s/def ::description (s/and #(not (nil? %)) #(<= 1 (count %))))

(defn title-valid? [title] (s/valid? ::title title))
(defn date-valid? [date] (s/valid? ::date date))
(defn desc-valid? [description] (s/valid? ::description description))

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

;;init functions
(defn add-participants
  []
  (go (let [response (<! (http/get "/api/users"))
            participants (re-frame/subscribe [::subs/participants])]
        (re-frame/dispatch [::events/set-participants (:body response)]))))

;;events
(defn call-save-api-fn
  [title date]
  (let [selected-participants (re-frame/subscribe [::subs/selected-participants])
        request-map {:json-params {:title title
                                   :review_date date
                                   :from_uid "U1"
                                   :participants @selected-participants}}
        all-fields-valid? (re-frame/subscribe [::subs/all-fields-valid?])]
    (re-frame/dispatch [::events/all-fields-valid-change (and
                                                          (date-valid? date)
                                                          (title-valid? title))])
    (when @all-fields-valid?
      (go (let [response (<! (http/post "/api/review-event" request-map))]
            (js/console.log (:body response)))))))

(defn toggle-participants
  [checked? id]
  (let [selected-participants [::subs/selected-participants]]
    (if checked?
      (re-frame/dispatch [::events/add-to-selected-participants id])
      (re-frame/dispatch [::events/remove-from-selected-participants id]))))

;;components
(defn participants-checkboxes
  [participants]
  (let [selected-participants (re-frame/subscribe [::subs/selected-participants])]
    [:div
     (for [participant @participants]
       ^{:key (participant :id)}
       [:div.checkbox (use-style checkbox-style)
        [:input
         {:type "checkbox"
          :id (participant :id)
          :name "checkbox"
          :value (participant :id)
          :on-change #(toggle-participants (-> % .-target .-checked) (-> % .-target .-value))}]
        [:label
         {:for (participant :id)} (participant :name)]])]))

;;main code
(defn review-event []
  (let [review-title (re-frame/subscribe [::subs/review-event-title])
        review-date (re-frame/subscribe [::subs/review-date])
        all-fields-valid? (re-frame/subscribe [::subs/all-fields-valid?])
        call-save-api #(call-save-api-fn @review-title @review-date)
        participants (re-frame/subscribe [::subs/participants])
        selected-participants (re-frame/subscribe [::subs/selected-participants])]
    (add-participants)
    [:div.main-content (use-style main-content-style)
     [:div.side-section (use-style (section-style "20vw"))]
     [:div.main-section (use-style (section-style "80vw"))
      [:div#box-button (use-style box-button-style)
       [:input#title-box
        {:style {:padding "5px"
                 :border "5px white"
                 :box-shadow "5px 5px 10px #888888"
                 :font-size "large"
                 :width "80%"
                 :height "10vh"}
         :placeholder "Review Event Title"
         :on-change #(re-frame/dispatch [::events/title-change (-> % .-target .-value)])}]
       [:input#date-picker
        {:style   {:border "5px white"
                   :border-right "3px solid #f8337d"
                   :box-shadow "5px 5px 10px #888888"}
         :color "#f8337d"
         :type "date"
         :on-change #(re-frame/dispatch [::events/date-change (-> % .-target .-value)])}]]
      [:div#description]
      [:div.participants-box (use-style checkbox-area)
       [:b "Add Participants"]
       [participants-checkboxes participants]]
      (components/Button
       {:variant "contained"
        :onClick call-save-api
        :style {:margin "5px"
                :background "#f8337d"
                :color "white"}} "Save")
      (when-not @all-fields-valid?
        [:h3 {:style {:color "red"}} "*Fill all fields"])]]))
