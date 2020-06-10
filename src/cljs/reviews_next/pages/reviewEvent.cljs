(ns reviews-next.pages.reviewEvent
  (:require
    [re-frame.core :as re-frame]
    [reviews-next.db :as db]
    [reviews-next.events :as events]
    [reviews-next.subs :as subs]))


(defn review_event []
      (let [review-title (re-frame/subscribe [::subs/review_event_title])
            review-date (re-frame/subscribe [::subs/review_date])]

        [:div#home {
                    :style {
                            :display "flex"
                            :flex-direction "row"}}
          (apply (.-log js/console) (str "Review Title " @review-title))
          (apply (.-log js/console) (str "Date " @review-date))
          ; [:h2 "Review Title " @review-title]
          ; [:h3 "Date " @review-date]
          [:div#side-section {
                              :style {
                                      :width "20vw"
                                      :margin "40px"}}]

          [:div#main-section {
                              :style {
                                      :width "80vw"
                                      :margin "40px"}}

           [:div#box-button {
                              :style {
                                      :display "flex"
                                      :flex-direction "row"
                                      :justify-content "space-between"
                                      :text-align "center"}}

            [:input#title-box {
                               :style {
                                       :padding "5px"
                                       :border "5px white"
                                       ; :border-right "3px solid #f8337d"
                                       :box-shadow "5px 5px 10px #888888"
                                       :font-size "large"
                                       :width "80%"
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
                                 :on-change #(re-frame/dispatch [::events/date-change (-> % .-target .-value)])}]]]]))
