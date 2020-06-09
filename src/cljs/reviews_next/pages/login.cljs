(ns reviews-next.pages.login)

(defn login []
  [:div.container
   [:div
     {:class "g-signin2"
          :data-onsuccess "onSignIn"}]])
