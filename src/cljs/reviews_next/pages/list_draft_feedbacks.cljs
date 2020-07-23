(ns reviews-next.pages.list-draft-feedbacks
  (:require
   ["@material-ui/core" :as material-ui]
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

(def level-style
  {:display "flex"
   :flex-direction "row"
   :margin "20px"
   :margin-left "0px"
   :width "20vw"
   :justify-content "flex-start"})

(def feedback-box-title
  {:display "flex"
   :flex-direction "row"
   :margin "2px 0px 2px 0px"
   :line-height "49px"
   :font-size "24px"
   :width "55vw"
   :justify-content "space-between"})

(def row-style
  {:display "flex"
   :flex-direction "row"
   :justify-content "space-evenly"
   :padding-inline "1em"
   :padding-block "0.2em"
   :width "100%"
  })

(defn feedback-markdown
  []
  [components/MarkDownEditor
   {:style {:width "100%"
            :height "50vh"
            :margin-block-end "2em"}
    :placeholder "Add Feedback"
    :value @(re-frame/subscribe [::subs/draft-review-description])
    :onChange  #(re-frame/dispatch [::events/draft-review-description (. %4 getText)])}])

(defn edit-feedback [id]
  (let [current-feedback-to-edit (re-frame/subscribe [::subs/current-feedback-to-edit])
        draft-review-description (re-frame/subscribe [::subs/draft-review-description])
        draft-level (re-frame/subscribe [::subs/draft-level])]
    [:div.main-content (use-style main-content-style)
     [:div.side-section (use-style (section-style "20vw"))]
     [:div.main-section (use-style (section-style "80vw"))
      [:div.feedback-user-display (use-style feedback-box-title)
       [:h3 "Feedback to " (:name (first (:reviewee @current-feedback-to-edit)))]
       [:div.level 
        [:label {:for "level"} "Level: "]
        [:input {:type "number"
                 :step 0.1
                 :id "level"
                 :value @draft-level
                 :on-change #(re-frame/dispatch [::events/draft-level (-> % .-target .-value)])}]]
       ]
      (js/console.log "curr " @current-feedback-to-edit)
       [:h3 "For Review Event: "  (:title (first (:review-event @current-feedback-to-edit)))]
       [:div#markdown
        [components/MarkDownEditor
         {:style {:width "100%"
                  :height "50vh"
                  :margin-block-end "2em"}
          :placeholder "Add Feedback"
          :value @draft-review-description
          :onChange  #(re-frame/dispatch [::events/draft-review-description (. %4 getText)])}]]
   
      ;; [:div.buttons (use-style linear-buttons)
      ;;  [draft-publish-buttons logged-in-user-id]]
       ]]
    ))


(defn feedbacks-table-component [logged-in-user-details] 
  (let [draft-feedback-list (re-frame/subscribe [::subs/draft-feedbacks-list])]
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
      (js/console.log "feedback: " @draft-feedback-list)
      (for [feedback @draft-feedback-list]
        ^{:key (:id feedback)}
        [:div.row (use-style row-style)
          ;;  (js/console.log feedback)
         [:i {:class "material-icons"
              :on-click #((re-frame/dispatch [::events/draft-review-description (:feedback (:feedback feedback))])
                          (js/console.log "feed " (:level (:feedback feedback)))
                          (re-frame/dispatch [::events/current-feedback-to-edit feedback])
                          (re-frame/dispatch [::events/draft-level (:level (:feedback feedback))])
                          (accountant/navigate! (str "/edit-feedback/" (:id feedback))))}  "edit"]
         [:div {:style {:width "30%"}} (:name (first (:reviewee feedback)))]
         [:div {:style {:width "40%"
                        :display "flex"
                        :flex-direction "row"
                        :justify-content "space-around"}}
          [:div (:level (first (:feedback feedback)))]
          [:div (count (:feedback (:feedback feedback))) " words"]
          [:div (:title (first (:review-event feedback)))]]
         
         [:div {:style {:width "20%"}}  (:review_date (first (:review-event feedback)))]
         [components/Button {:onClick #((re-frame/dispatch [::events/publish-feedback feedback]))
                             :width "20%"
                             :style {:margin "0px 5px"
                                     :color "#00947E"}} "Publish"]])]]))
        

(defn list-draft-feedback []
  (reagent/with-let [logged-in-user-details {:id "U1" :name "ABC"}]
   (reagent/create-class
    {:component-did-mount
     (fn []
       (js/console.log "Call Feedback list")
       (re-frame/dispatch [::events/populate-draft-feedback-list (:id logged-in-user-details)]))
     :display-name "Main component"
     :reagent-render
     (fn []
       [:div.main-content (use-style main-content-style)
          [:div.side-section (use-style (section-style "20vw"))]
          [:div.main-section (use-style (section-style "80vw"))
            [:div.heading (use-style heading-style)
              [:a {:style
                   {:font-size "36px"}} (:name logged-in-user-details) " ( You)"]]
            [feedbacks-table-component logged-in-user-details]
            ]])})))
