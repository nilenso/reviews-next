(ns reviews-next.routes
  (:require [bidi.bidi :as b]
            [reviews-next.handlers.pages :as pages]
            [reviews-next.handlers.ping :as ping]
            [reviews-next.handlers.users :as users]))

(def routes
  ["/" [["" pages/index]
        ["home" pages/home]
        ["users/" {"login" {:post users/login}}]
        ["ping" ping/ping]
        ["oops" pages/oops]
        [true pages/not-found]]])

(def path-for (partial b/path-for routes))
