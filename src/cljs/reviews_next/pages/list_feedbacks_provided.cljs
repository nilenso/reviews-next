(ns reviews-next.pages.list-feedbacks-provided
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [stylefy.core :as stylefy :refer [use-style]]
   [reviews-next.events :as events]
   [reviews-next.subs :as subs]
   [accountant.core :as accountant]
   [reviews-next.components :as components]))

;;styles
(def main-content-style
  {:display "flex"
   :flex-direction "row"})

(defn section-style [width]
  {:margin "40px"
   :width width})

(def heading-style
  {:line-height "42px"})

(def table-style
  {:display "flex"
   :flex-direction "column"
   :box-shadow "0px 4px 4px rgba(0, 0, 0, 0.25)"
   :border "1px solid #FEFEFE"
   :border-radius "4px"
  })

(def head-row-style
  {:display "flex"
   :flex-direction "row"
   :justify-content "space-evenly"
   :padding-inline "1em"
   :padding-block "0.5em"
   :width "100%"
  })

(def row-style
  {:display "flex"
   :flex-direction "row"
   :justify-content "space-evenly"
   :padding-inline "1em"
   :padding-block "0.2em"
   :width "100%"
  })

(defn feedbacks-table-component [logged-in-user-details] 
  (reagent/with-let [feedback-list (re-frame/subscribe [::subs/feedbacks-list])]
    (reagent/create-class
     {:component-did-mount
      (fn []
        (js/console.log "Call Feedback list")
        (re-frame/dispatch [::events/populate-feedback-list (:id logged-in-user-details)]))
      :display-name "Table component"
      :reagent-render
      (fn []
        [:div
         [:div {:style {:background "#EFFAF3"
                        :color "#1D72AA"
                        :text-align "center"
                        :line-height "33px"}} "Reviews"]
         [:div.feedback-table (use-style table-style)
          [:div.head-row (use-style head-row-style)
           [:div {:style {:width "10%"}}]
           [:div {:style {:width "25%" :text-align "center"}} "Pending"]
           [:div {:style {:width "35%" :text-align "center"}} "Drafts"]
           [:div {:style {:width "10%" :text-align "center"}} "Published"]
           [:div {:style {:width "20%"}}]]
          (js/console.log "feedback: " @feedback-list)
          (for [feedback @feedback-list]
            ^{:key (:id feedback)}
            [:div.row (use-style row-style)
             [:i {:class "material-icons"} "check"]
             [:div {:style {:width "30%"}} (:name (first (:reviewee feedback)))]
             [:div {:style {:width "40%"
                            :display "flex"
                            :flex-direction "row"
                            :justify-content "space-around"}}
              [:div (:level (first (:feedback feedback)))]
              [:div (:title (first (:review-event feedback)))]]
             [:div {:style {:width "20%"}}  (:review_date (first (:review-event feedback)))]
             [components/Button {:onClick #((re-frame/dispatch-sync [::events/unpublish-feedback (:id feedback)])
                                            (re-frame/dispatch-sync [::events/populate-feedback-list (:id logged-in-user-details)]))
                                 :width "20%"
                                 :style {:margin "0px 5px"
                                         :color "#00947E"}} "Unpublish"]])]])})))
        

(defn start-component []
  (let [logged-in-user-details {:id "U1" :name "ABC"}]
    [:div.main-content (use-style main-content-style)
     [:div.side-section (use-style (section-style "20vw"))]
     [:div.main-section (use-style (section-style "80vw"))
      [:div.heading (use-style heading-style)
       [:a {:style
            {:font-size "36px"}} (:name logged-in-user-details) " ( You)"]]
      [feedbacks-table-component logged-in-user-details]]]))
