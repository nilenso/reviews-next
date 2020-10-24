(ns reviews-next.routes
  (:require [bidi.bidi :as b]
            [reviews-next.handlers.pages :as pages]))

(def routes
  ["/" [["" pages/index]
        ["home" pages/home]
        [true pages/not-found]]])

(def path-for (partial b/path-for routes))
