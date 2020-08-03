(ns reviews-next.events.user
  (:require [re-frame.core :as re-frame]
            [day8.re-frame.http-fx]
            [ajax.core :as ajax]))

(defn login-payload [google-user]
  (let [profile (.getBasicProfile google-user)
        google-id (.getId profile)
        name (.getName profile)
        image-url (.getImageUrl profile)
        email (.getEmail profile)]
    {:google-id google-id
     :name name
     :image-url image-url
     :email email}))

(re-frame/reg-event-fx
 ::setup-google-signin-functions
 (fn [_ _]
   (set!
    (.. js/window -onSignIn)
    (clj->js #(re-frame/dispatch [::login-user (login-payload %)])))
   {}))

(re-frame/reg-event-fx
 ::login-user
 (fn [_ [_ payload]]
   {:http-xhrio
    {:method :post
     :uri "/api/google/login"
     :params payload
     :format (ajax/json-request-format)
     :response-format (ajax/json-request-format {:keywords? true})
     :on-success []
     :on-fail []}}))

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
