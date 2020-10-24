(ns reviews-next.handlers.core
  (:require [hiccup.core :as h]
            [hiccup.page :as page]
            [reviews-next.config :as config]
            [reviews-next.components.common :as common]
            [ring.util.response :as response]))

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1"}]
   [:meta {:name "google-signin-client_id"
           :content (-> config/config :secrets :google-oauth :client-id)}]
   [:link {:rel "icon" :type "image/png" :href "/images/favicon.png"}]
   (page/include-css "/css/font-awesome.min.css")
   (page/include-css "/css/simplemde.min.css")
   (page/include-css "/css/bulma.min.css")
   (page/include-css "/css/bulma.min.css")
   (page/include-css "/css/unpoly.css")
   (page/include-css "/css/site.css")
   (page/include-js "https://apis.google.com/js/platform.js")
   (page/include-js "/js/unpoly.js")
   (page/include-js "/js/simplemde.min.js")
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

(defn logged-in-page [{:keys [body] :as args}]
  (let [logged-in-body
        [:section {:class "section"}
         (common/header)
         [:section {:class "section"}
          [:div {:class "tile is-ancestor"}
           [:div {:class "tile is-2 is-vertical"}
            (common/menu)]
           [:div {:class "tile is-10"}
            body]]]]]
    (page (assoc args :body logged-in-body))))
