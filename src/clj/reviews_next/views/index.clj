(ns reviews-next.views.index)

(defn page []
  [:div#app
   [:div.login
    [:img {:src "/images/vertical.svg"}]
    [:div
     {:class "g-signin2"
      :data-width "240"
      :data-heigth "50"
      :data-theme "dark"
      :data-longtitle "240"
      :data-onsuccess "onSignIn"}]]])
