(ns reviews-next.components.common)

(defn header []
  [:nav {:class "navbar is-primary is-fixed-top"
         :role "navigation"
         :aria-label "main navigation"}

   [:div {:class "navbar-brand"}
    [:a {:class "navbar-item"
         :href "/home"}
     [:img {:src "/images/logo-horiz-inverted.svg"}]]]

   [:div {:class "navbar-menu"}
    [:div {:class "navbar-end"}
     [:a {:class "navbar-item" :href "/review-event/new"} "+ Review Event"]
     [:a {:class "navbar-item" :href "/profile"} "Profile"]
     [:div {:class "navbar-item"}
      [:a {:class "buttons"}
       [:a {:class "button is-warning" :href "/logout"}
        [:span {:class "icon"}
         [:i {:class "fas fa-sign-out"}]]
        "Logout"]]]]]])

(defn menu []
  [:aside {:class "menu"}
   [:ul {:class "menu-list"}
    [:li [:a "Users"]]
    [:li [:a "Review Events"]]
    [:li [:a "Feedback For You"]]
    [:li [:a "Feedback By You"]]]])
