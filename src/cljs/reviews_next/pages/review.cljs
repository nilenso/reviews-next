(ns reviews-next.pages.review
  (:require
    [re-frame.core :as re-frame]
    [cljs.core.async :refer [<!]]
    [cljs-http.client :as http]
    [clojure.pprint :as pp]
    [stylefy.core :as stylefy :refer [use-style]]
    [reviews-next.db :as db]
    [reviews-next.events :as events]
    [reviews-next.subs :as subs])
  (:require-macros
    [cljs.core.async.macros :refer [go]]))

(def main-content-style
           {:display "flex"
            :flex-direction "row"})

(defn section-style [width]
               {:margin "40px"
                :width width})
(def box-button-style
  {
   :display "flex"
   :flex-direction "row"
   :justify-content "space-between"
   :text-align "center"})

(defn call-save-api-fn
  [title date]
  (go (let [response (<! (http/get (str "http://localhost:3000/api/post-title-date/" title "/" date)))]
        (js/console.log (:body response)))))

(defn review-event []
      (let [review-title (re-frame/subscribe [::subs/review-event-title])
            review-date (re-frame/subscribe [::subs/review-date])
            call-save-api #(call-save-api-fn @review-title @review-date)]
        [:div.main-content (use-style main-content-style)
           [:div.side-section (use-style (section-style "20vw"))]

          [:div.main-section (use-style (section-style "80vw"))
           [:div#box-button (use-style box-button-style)

            [:input#title-box
                              {:style {
                                       :padding "5px"
                                       :border "5px white"
                                       :box-shadow "5px 5px 10px #888888"
                                       :font-size "large"
                                       :width "80%"
                                       :height "10vh"}
                               :placeholder "Review Event Title"
                               :on-change #(re-frame/dispatch [::events/title-change (-> % .-target .-value)])}]
            [:input#date-picker
                                {:style   {
                                           :border "5px white"
                                           :border-right "3px solid #f8337d"
                                           :box-shadow "5px 5px 10px #888888"}
                                 :color "#f8337d"
                                 :type "date"
                                 :on-change #(re-frame/dispatch [::events/date-change (-> % .-target .-value)])}]]
           [:input#save-buton
                             {:type "button"
                              :value "Save"
                              :on-click call-save-api}]]]))
