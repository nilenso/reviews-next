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

(defn clear-all-fields
  []
  (re-frame/dispatch [::events/date-change ""])
  (re-frame/dispatch [::events/title-change ""])
  (re-frame/dispatch [::events/description-change ""])
  (re-frame/dispatch [::events/all-fields-valid-change false])
  (re-frame/dispatch [::events/remove-all-selected-participants]))

(defn call-save-api-fn
  [title date description selected-participants ]
  (js/console.log "Coming here")
  (let [request-map {:json-params {:title title
                                   :review_date date
                                   :review_description description
                                   :from_uid "U1"
                                   :participants selected-participants}}
        all-fields-valid? (and
                           (date-valid? date)
                           (title-valid? title)
                           (desc-valid? description))]
    (when all-fields-valid?
      (go (let [response (<! (http/post "/api/review-event" request-map))]
            (js/console.log (str "review event api response: " (:body response))))))
    (clear-all-fields)))

(defn toggle-participants
  [checked? id]
  (js/console.log id)
  (let [selected-participants [::subs/selected-participants]]
    (if checked?
      (re-frame/dispatch [::events/add-to-selected-participants id])
      (re-frame/dispatch [::events/remove-from-selected-participants id]))))

;;components
(defn participants-checkboxes
  [participants]
  (let [selected-participants (re-frame/subscribe [::subs/selected-participants])]
    (fn []
      [:div
       (for [participant participants]
         ^{:key (participant :id)}
         (let [checked? (some #(= (participant :id) %) @selected-participants)]
           [:div.checkbox (use-style checkbox-style)
            [:input
             {:type "checkbox"
              :id (participant :id)
              :name "checkbox"
              :value (participant :id)
              :checked checked?
              :on-change #(toggle-participants (-> % .-target .-checked) (-> % .-target .-value))}]
            [:label
             {:for (participant :id)} (participant :name)]]))])))

;;main code
(defn review-event []
  (let [review-title (re-frame/subscribe [::subs/review-event-title])
        review-date (re-frame/subscribe [::subs/review-date])
        review-description (re-frame/subscribe [::subs/review-description])
        all-fields-valid? (re-frame/subscribe [::subs/all-fields-valid?])
        selected-participants (re-frame/subscribe [::subs/selected-participants])
        call-save-api #(call-save-api-fn @review-title @review-date @review-description @selected-participants)
        participants (re-frame/subscribe [::subs/participants])]
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
          :value @review-title
          :placeholder "Review Event Title"
          :on-change #(re-frame/dispatch [::events/title-change (-> % .-target .-value)])}]
        [:input#date-picker
         {:style   {:border "5px white"
                    :border-right "3px solid #f8337d"
                    :box-shadow "5px 5px 10px #888888"}
          :color "#f8337d"
          :type "date"
          :value @review-date
          :on-change #(re-frame/dispatch [::events/date-change (-> % .-target .-value)])}]]
       [:div.markdown {
          :style {:padding "10px"
                  :border "5px white"
                  :box-shadow "2px 2px px #888888"
                  }}
        (components/MarkDownEditor
         {:style {:width "80%"
                  :height "50vh"
                  :padding-bottom "20px"}
          :value @review-description
          :placeholder "Add description"
          :onChange  #(re-frame/dispatch [::events/description-change (. %4 getText)])
          })
        ]     
       [:div.participants-box (use-style checkbox-area)
        [:b "Add Participants"]]
         [participants-checkboxes @participants @selected-participants]
       (components/Button
        {:variant "contained"
         :onClick call-save-api
         :style {:margin "5px"
                 :background "#f8337d"
                 :color "white"}} "Save")
       (when-not @all-fields-valid?
         [:h3 {:style {:color "red"}} "*Fill all fields."])]]))
