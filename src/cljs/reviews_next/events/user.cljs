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
             id (.getId profile)
             name (.getName profile)
             image-url (.getImageUrl profile)
             email (.getEmail profile)]
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
