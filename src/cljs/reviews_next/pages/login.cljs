(ns reviews-next.pages.login)

(defn login []
  [:div.login
   [:img {:src "/assets/images/vertical.svg"}]
   [:div
    {:class ["g-signin2"]
     :data-width "240"
     :data-heigth "50"
     :data-theme "dark"
     :data-longtitle "240"
     :data-onsuccess "onSignIn"}]])
