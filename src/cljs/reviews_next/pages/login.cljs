(ns reviews-next.pages.login)

(defn logo []
  [:img {:src "assets/images/vertical.svg"}])

(defn login-button []
  [:div {:class ["g-signin2"]
         :data-onsuccess "onSignIn"
         :data-width "240"
         :data-heigth "50"
         :data-theme "dark"
         :data-longtitle "240"}])

(defn login []
  [:div {:class "login container" }
   [logo]
   [login-button]])
