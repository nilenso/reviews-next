(ns reviews-next.handlers.pages
  (:require [hiccup.core :as h]
            [hiccup.page :as page]))

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1"}]
   (page/include-css "/public/css/site.css")])

(defn page [contents]
  (page/html5
   (head)
   [:body
    contents
    (page/include-js "/public/js/app.js")]))

(defn index [_request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (h/html (page [:div "Hello World!"]))})

(defn not-found [_request]
  {:status 404
   :headers {"Content-Type" "text/html"}
   :body (h/html
          (page/html5
           [:head]
           [:body
            [:div "Not found"]]))})
