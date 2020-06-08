(ns reviews-next.routes
  (:require [bidi.ring :as bidi]
            [reviews-next.handlers.pages :as pages]))

(def routes
  ["/" {"" pages/index
        "assets" (bidi/resources {:prefix "assets/"})
        true pages/not-found}])
