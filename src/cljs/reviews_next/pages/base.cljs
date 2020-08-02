(ns reviews-next.pages.base)

(defn header [user]
  [:div.header
   [:img {:src "/assets/images/header-logo.svg"}]
   [:div.buttons
    [:button {:class ["primary"]} "+ Review Event"]
    [:button {:class ["link"]} "Profile"]
    [:button {:class ["warning"]} (str "Sign out as " (:name user))]]])

(defn nav-item [name]
  [:li
   [:i {:class ["fa" "fa-chevron-right"]}]
   [:u name]])

(defn navigation []
  [:div.navigation
   [:ul
    [nav-item "Users"]
    [nav-item "Review Events"]
    [nav-item "Feedback for you"]
    [nav-item "Feedback given by you"]]])

(defn base [user contents]
  [:div.container
   [header user]
   [:div.contents
    [navigation]
    [:div.panel [contents]]]])
