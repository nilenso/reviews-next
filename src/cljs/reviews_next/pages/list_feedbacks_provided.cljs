(ns reviews-next.pages.list-feedbacks-provided
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

;; (defn display table [title head body style]
;;   [:> material-ui/TableContainer {:style style :align "center"}  title
;;    [:> material-ui/Table {:size "small" :style {:background "white"}}
;;     [:> material-ui/TableHead
;;      [:> material-ui/TableRow
;;       (for [head-col head]
;;         [:> material-ui/TableCell {:align "left"} (head-col)])]]
;;     [:> material-ui/TableBody
;;      (for [body-row body]
;;        [:> material-ui/TableRow
;;         [:> material-ui/TableCell
;;          {:align "left" :style {:font-size "12px" :margin "2px"}}
;;          (components/Button (first body-row) (last body-row))]
;;         [:> material-ui/TableCell {:align "right" :style {:font-size "12px"}}
;;          (second body-row)]])]]])

(defn feedbacks-table-component [logged-in-user-details] 
  (let [_ (re-frame/dispatch [::events/populate-feedback-list (:id logged-in-user-details)])
        feedback-list (re-frame/subscribe [::subs/feedbacks-list])]
    [:div 
      [:div {:style {:background "#EFFAF3"
                    :color "#1D72AA"
                    :text-align "center"
                    :line-height "33px"}} "Reviews"]
       [:div.feedback-table (use-style table-style)
        [:div.head-row (use-style head-row-style)
          [:div {:style {:width "10%"}}]
          [:div {:style {:width "25%" :text-align "center"}} "Pending" ]
          [:div {:style {:width "35%" :text-align "center"}} "Drafts" ]
          [:div {:style {:width "10%" :text-align "center"}} "Published" ]
          [:div {:style {:width "20%"}}]]
        (js/console.log "feedback: " @feedback-list)
        (for [feedback @feedback-list]
          ^{:key (:id feedback)}
          [:div.row (use-style row-style)
          ;;  (js/console.log feedback)
           [:i {:class "material-icons"} "check"]
           [:div {:style {:width "30%"}} (:name (first (:reviewee feedback)))]
           [:div {:style {:width "40%"
                          :display "flex"
                          :flex-direction "row"
                          :justify-content "space-around"}}
            [:div (:level (first (:feedback feedback)))]
            [:div (:title (first (:review-event feedback)))]]
           [:div {:style {:width "20%"}}  (:review_date (first (:review-event feedback)))]
           [components/Button {:onClick #(re-frame/dispatch [::events/unpublish-feedback (:id feedback)])
                               :width "20%"
                               :style {:margin "0px 5px"
                                       :color "#00947E"}} "Unpublish"]])
        
        ]]))
        

(defn start-component []
  (reagent/with-let [logged-in-user-details {:id "U1" :name "ABC"}]
   (reagent/create-class
    {:component-did-mount
     (fn []
       (js/console.log "Call Feedback list")
       (re-frame/dispatch [::events/populate-feedback-list (:id "logged-in-user-details")]))
     :display-name "Main component"
     :reagent-render
     (fn []
       [:div.main-content (use-style main-content-style)
          [:div.side-section (use-style (section-style "20vw"))]
          [:div.main-section (use-style (section-style "80vw"))
            [:div.heading (use-style heading-style)
              [:h1 {:style 
                      {:font-size "36px"}} (:name logged-in-user-details) " ( You)"]]
            [feedbacks-table-component logged-in-user-details]
            ]])})))
