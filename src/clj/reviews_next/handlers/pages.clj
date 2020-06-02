(ns reviews-next.handlers.pages
  (:require [hiccup.core :as h]
            [hiccup.page :as page]
            [reviews-next.config :as config]))

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1"}]
   [:meta {:name "google-signin-client_id"
           :content (-> config/config :secrets :google-oauth :client-id)}]
   (page/include-css "/public/css/site.css")])

(defn page [contents]
  (page/html5
   (head)
   [:body
    contents
    (page/include-js "/public/js/app.js")
    (page/include-js "https://apis.google.com/js/platform.js")]))

(defn index [_request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (h/html (page [:div#container]))})

(defn not-found [_request]
  {:status 404
   :headers {"Content-Type" "text/html"}
   :body (h/html
          (page/html5
           [:head]
           [:body
            [:div "Not found"]]))})
