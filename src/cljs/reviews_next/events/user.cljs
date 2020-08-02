(ns reviews-next.events.user
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-event-fx
 ::setup-google-signin-functions
 (fn [_ _]
   (set!
    (.. js/window -onSignIn)
    (clj->js
     (fn [user]
       (let [profile (.getBasicProfile user)
             ^int id (.getId profile)
             ^string name (.getName profile)
             ^string image-url (.getImageUrl profile)
             ^string email (.getEmail profile)]
         (re-frame/dispatch
          [::login-user
           {:id id
            :name name
            :image-url image-url
            :email email}])))))
   {}))

(re-frame/reg-event-db
 ::login-user
 (fn [db [_ user]]
   (assoc db :user user)))

(re-frame/reg-event-db
 ::clear-user
 (fn [db [_ user]]
   (assoc db :user user)))

(re-frame/reg-event-fx
 ::sign-out
 (fn [_ _]
   (let [auth-instance (.getAuthInstance (.-auth2 (.-gapi js/window)))]
     (.then (.signOut auth-instance)
            #(re-frame/dispatch [::clear-user]))
     {})))
