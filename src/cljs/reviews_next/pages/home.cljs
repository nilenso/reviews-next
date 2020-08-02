(ns reviews-next.pages.home)

(defn logo []
  [:div.container
   [:img {:src "/assets/images/vertical.svg"}]])

(defn login []
  [:div.container
   [:div
    {:class "g-signin2"
      :data-onsuccess "onSignIn"}]])

(defn home []
  [:div
   [logo]
   [login]])
