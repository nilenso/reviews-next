(ns reviews-next.pages.base)

(defn header []
  [:div.header
   [:img {:src "/assets/images/header-logo.svg"}]
   [:div.buttons
    [:button {:class ["primary"]} "+ Review Event"]
    [:button {:class ["link"]} "Profile"]
    [:button {:class ["warning"]} "Sign Out"]]])
