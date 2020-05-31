(ns reviews-next.routes
  (:require [bidi.ring :as bidi]
            [reviews-next.handlers.pages :as pages]))

(def routes
  ["/" {"" pages/index
        "public" (bidi/resources {:prefix "public/"})
        true pages/not-found}])
