(ns reviews-next.pages.review_event
  (:require
    [re-frame.core :as re-frame]
    [reviews-next.db :as db]
    [reviews-next.events :as events]
    [reviews-next.subs :as subs]))


(defn review_event []
  (let [review-title (re-frame/subscribe [::subs/review_event_title])
        review-date (re-frame/subscribe [::subs/review_date])
        review-level (re-frame/subscribe [::subs/review_level])
        gettext-fn #(-> % .-target .-value)
        title-change-fn #(re-frame/dispatch [::events/title-change gettext-fn])]

    [:div#home {
                :style {
                        :margin "40px"
                        :display "flex"
                        :flex-direction "column"}}

      [:h2 "Review title " @review-title]
      [:h3 "Date " @review-date]
      [:div#box-button {
                         :style {
                                 :display "flex"
                                 :flex-direction "row"
                                 :justify-content "space-between"
                                 :text-align "center"}}

       [:input#title-box {
                          :style {
                                  :border "5px white"
                                  :border-right "3px solid #f8337d"
                                  :box-shadow "5px 5px 10px #888888"
                                  :font-size "large"
                                  :width "80vw"
                                  :height "10vh"}
                          :placeholder "Review Event Title"
                          :on-change #(re-frame/dispatch [::events/title-change (-> % .-target .-value)])}]
       [:input#date-picker {
                            :style {
                                    :border "5px white"
                                    :border-right "3px solid #f8337d"
                                    :box-shadow "5px 5px 10px #888888"}
                            :color "#f8337d"
                            :type "date"
                            :on-change #(re-frame/dispatch [::events/date-change (-> % .-target .-value)])}]]
      [:textarea {
                  :style {
                          :border "5px white"
                          :border-right "3px solid #f8337d"
                          :box-shadow "5px 5px 10px #888888"
                          :margin "20px 0px 20px 0px"
                          :padding "10px"
                          :font-size "large"
                          :resize "None"}
                  :rows "4"
                  ; :cols "50"
                  ; :type "text"
                  :placeholder "Enter Description"
                                ; :value @(re-frame/subscribe [::subs/name])
                  :on-change #(re-frame/dispatch [::events/description-change (-> % .-target .-value)])}]
      [:div "Level " @review-level]
      [:input#level {
                     :style {
                             :background "#f8337d"}
                     :type "range"
                     :step "0.1"
                     :min "2.0"
                     :max "6.5"
                     :on-change #(re-frame/dispatch [::events/level-change (-> % .-target .-value)])}]]))
