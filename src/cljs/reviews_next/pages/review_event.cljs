(ns reviews-next.pages.review_event)

(defn review_event []
  [:div#home
    [:h2 "Review event page"]
    [:div#box-button {
                       :style {
                               :display "flex"
                               :flex-direction "row"
                               :justify-content "space-evenly"
                               :text-align "center"}}

     [:input#title-box {
                        :style {
                                :width "80vw"
                                :height "10vh"}
                        :placeholder "Review Event Title"}]
     [:input#date-picker {
                          :type "date"}]]






    [:h1 "Hi"]])
