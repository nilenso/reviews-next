(ns reviews-next.pages.base
  (:require [re-frame.core :as re-frame]
            [reviews-next.events.user :as user-events]))

(defn header [user]
  [:div.header
   [:img {:src "/assets/images/header-logo.svg"}]
   [:div.buttons
    [:button {:class ["primary"]} "+ Review Event"]
    [:button {:class ["link"]} "Profile"]
    [:button
     {:class ["warning"]
      :on-click #(re-frame/dispatch [::user-events/sign-out])}
     (str "Sign out as " (:name user))]]])

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
