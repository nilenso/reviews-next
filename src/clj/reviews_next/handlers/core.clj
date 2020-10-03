(ns reviews-next.handlers.core
  (:require [hiccup.core :as h]
            [hiccup.page :as page]
            [reviews-next.config :as config]
            [ring.util.response :as response]))

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1"}]
   [:meta {:name "google-signin-client_id"
           :content (-> config/config :secrets :google-oauth :client-id)}]
   (page/include-css "/css/bulma.min.css")
   (page/include-css "/css/unpoly.css")
   (page/include-css "/css/site.css")
   (page/include-js "https://apis.google.com/js/platform.js")
   (page/include-js "/js/unpoly.js")
   [:title "Reviews - Nilenso"]])

(defn html-body [body]
  (page/html5
    (head)
    [:body
     body
     (page/include-js "/js/site.js")]))

(defn page [{:keys [status body]}]
  (-> (html-body body)
      h/html
      response/response
      (response/status (or status 200))
      (response/content-type "text/html")))
