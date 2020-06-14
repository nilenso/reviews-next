(ns reviews-next.routes
  (:require [bidi.ring :as bidi]
            [ring.adapter.jetty :as jetty]
            [reviews-next.handlers.pages :as pages]
            [ring.middleware.cors :refer [wrap-cors]]
            [reviews-next.handlers.post-title-date :as post-title-date]))

(def handler
  (wrap-cors routes :access-control-allow-origin [#"http://localhost:3000/"]
                       :access-control-allow-methods [:get :put :post :delete]))

(def routes
  ["/" {"" pages/index
        "api/post-title-date/" {[:title "/" :date ] post-title-date/index}
        "assets" (bidi/resources {:prefix "assets/"})
        true pages/not-found}])
