(ns reviews-next.pages.home)

(defn home [])

(defn logo []
  [:div.container
   [:h1 "Hello World!"]
   [:img {:src "target/assets/images/vertical.svg"}]])

(defn login []
  [:div.container
   [:div
    {:class "g-signin2"
      :data-onsuccess "onSignIn"}]])
