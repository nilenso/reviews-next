(ns reviews-next.handlers.users
  (:require [reviews-next.routes :as routes]
            [reviews-next.handlers.pages :as pages ]
            [ring.util.response :as response]))

(defn login [_request]
  (response/redirect (routes/path-for pages/home)))
